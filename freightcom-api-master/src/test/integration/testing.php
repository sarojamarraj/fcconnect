<?php

/**
 * API integration testing framework
 * Tests are described by PHP arrays that contain a set of instructions for
 * running and interpreting and validating the results of API commands.
 * Uses positional arguments - this is more concise and less verbose though it
 * sometimes makes it hard to get right the first time.
 *
 * The test array is returned by a static method called <test-name>::requests()
 *
 * The script uses the tests named on the command line - it can be a full path
 * to a PHP file with or without the .php extension, or just a class name in
 * which case the file is relative to tests in the integration directory.
 *
 * Individual tests abort on first failure, but subsequent tests are run.
 *
 * Without --debug, minimal output.
 *
 */

require_once('UsageException.php');
require_once('TestException.php');

try {

  (new TestRunner($argv))
    ->run()
    ->printTimes()
    ->printAverages()
    ->dumpCSV()
    ->summary();
} catch (UsageException $e) {
  fprintf(STDERR, "\n%s\n\nUsage: %s [--list] [--debug] [--times] [--csv file] [--curl-debug] [--runs runs] [--post-test <testname> ]host [ test1 test2 ...]\n", $e->getMessage(), basename($argv[0]));
  fprintf(STDERR, "Runs all tests in __DIR__/test if no tests\n");
  } catch (Exception $e) {
    fprintf(STDERR, "Error: %s\n", $e->getMessage());
    }

exit();


/**
 * Interpret x.y.z as a indexes into a PHP array.
 * Return the extracted value or null if path isn't set.
 *
 * Special components
 *   @size - returns the size of the array
 *
 * If path is callable, use the result of the callable
 */
function get_path($path, $object)
{
  if (is_callable($path)) {
    $result = $path($object);
  } else {
    $result = $object;
    $rest = $path;

    if (preg_match('/^([^.]+)\.(.*)$/ui', $rest, $matches)) {
      $component = $matches[1];
      $rest = $matches[2];
    } else {
      $component = $path;
      $rest = null;
    }

    while ($component !== null) {
      if ($result === null) {
        break;
      } else if ($component === '') {
        // skip
      } else if ($component === '*') {
        if (is_array($result)) {
          $values = [];

          foreach ($result as $item) {
            $values[] = get_path($rest, $item);
          }
        }

        $result = $values;
        break;
      } else if ($component === '@size') {
        if (is_array($result)) {
          $result = count($result);
        } else {
          $result = null;
        }
      } else if (isset($result[$component])) {
        $result = $result[$component];
      } else {
        $result = null;
        break;
      }

      if ($rest !== null && preg_match('/^([^.]+)\.(.+)$/ui', $rest, $matches)) {
        $component = $matches[1];
        $rest = $matches[2];
      } else {
        $component = $rest;
        $rest = null;
      }
    }
  }

  return $result;
}

/**
 * Update the values in the array by replacing patterns with replacements.
 *
 * Special case, if object is a string, look up the value in the map.
 * Special case, if object is callable, use the result of calling
 * it on map.
 */
function subst_object($object, array $patterns, array $replacements, $map, $nested = false)
{
  if ($object === null) {
    return null;
  } else if (is_string($object)) {
    if (! $nested) {
      // special case, look up in map if top level
      if (isset($map[$object])) {
        return $map[$object];
      } else {
        return str_replace($patterns, $replacements, $object);
      }
    } else {
      return str_replace($patterns, $replacements, $object);
    }
  } else if (is_callable($object)) {
    return $object($map);
  } else if (is_array($object)) {
    $result = [];

    foreach ($object as $key => $value) {
      $result[$key] = subst_object($value, $patterns, $replacements, $map, true);    }

    return $result;
  } else {
    return $object;
  }
}


class TestRunner
{
  protected $runCount = 0;
  protected $verifyPeer = true;
  protected $curlVerbose = false;
  protected $sessionID = null;
  protected $cookies = [];
  protected $label = null;
  protected $loadedTests = [];
  protected $headers;
  protected $error;
  protected $info;
  protected $host;
  protected $output;
  protected $map;
  protected $route;
  protected $verb;
  protected $csvFile = null;
  protected $postTest = null;
  protected $csv = [];
  protected $times = [];
  protected $cleaningUp = false;
  protected $time_histogram = [];
  public static $bucket_size = 50.0;
  public static $max_buckets = 40;
  public static $header_debug = false;
  protected $runs = 1;

  protected $average_times = [
                              'GET' => [],
                              'PUT' => [],
                              'POST' => [],
                              'DELETE' => [],
                              ];
  protected $show_times = false;
  protected $testPaths = [];
  protected $testName = '';
  protected $list = false;
  protected $failures = [];

  public static $debug = false;
  public static $curl_debug = false;
  public static $admin_password;

  /**
   *
   */
  public function __construct(array $argv)
  {
    self::$admin_password = getenv('FREIGHTCOM_ADMIN_PASSWORD');

    $csvFileExpected = false;
    $runsExpected = false;
    $expectPost = false;

    foreach (array_slice($argv, 1) as $arg) {
      if ($expectPost) {
        $expectPost = false;
        $this->postTest = $arg;
      } else if ($arg === '--post-test') {
        $expectPost = true;
      } else if ($runsExpected) {
        $this->runs = $arg;
        $runsExpected = false;
      } else if ($csvFileExpected) {
        $csvFileExpected = false;
        $this->csvFile = $arg;
      } else if ($arg === "--csv") {
        $csvFileExpected = true;
      } else if ($arg === '--times') {
        $this->show_times = true;
      } else if ($arg === '--runs') {
        $runsExpected = true;
      } else if ($arg === '--list') {
        $this->list = true;
      } else if ($arg === '--debug') {
        self::$debug = true;
      } else if ($arg === '--headers') {
        self::$header_debug = true;
      } else if ($arg === '--curl-debug') {
        self::$curl_debug = true;
      } else if (preg_match('/^--/ui', $arg)) {
        throw new UsageException("Bad argument: {$arg}");
      } else if (! isset($this->host)) {
        $this->host = $arg;
      } else {
        $this->testPaths[] = $arg;
      }
    }

    if (! isset($this->host)) {
      throw new UsageException("Missing host");
    }

    if (empty($this->testPaths)) {
      $this->findTests();
    }

    if (empty($this->testPaths)) {
      throw new UsageException("No tests specified");
    }
  }

  /**
   *
   */
  public function run()
  {
    if ($this->list) {
      $this->listTests();
    } else {
      for ($i = 0; $i < $this->runs; $i++) {
        $this->runTests();
      }
    }

    return $this;
  }

  /**
   *
   */
  public function listTests()
  {
    foreach ($this->testPaths as $testPath) {
      printf("%s\n", str_replace(__DIR__ . '/tests/', '', $testPath));
    }

    return $this;
  }

  /**
   *
   */
  public function getTest($path)
  {
    // Strip trailing commas from name
    if (preg_match('/^(.*),,*$/ui', $path, $matches)) {
      $path = $matches[1];
    }

    if (! isset($this->loadedTests[$path])) {
      if (! file_exists($path)) {
        $path = __DIR__ . '/tests/' . $path;
      }


      if (! preg_match('/\.php$/u', $path)) {
        $path .= '.php';
      }

      $included = @include_once($path);

      if ($included === false) {
        throw new UsageException("Unable to load test {$path}");
      }

      $this->loadedTests[$path] = basename($path, '.php');
    }

    return $this->loadedTests[$path];
  }

  /**
   * Run each test in the list. Run postTest after each if
   * specified.
   */
  public function runTests()
  {
    $this->csv[] = [ (new DateTime())->format('Y-m-d H:i') ];
    $count = 1;

    foreach ($this->testPaths as $testPath) {
      $testName = $this->getTest($testPath);

      if (is_callable([ $testName, 'clearCaches' ], false, $callable)) {
        $callable();
      }

      $this->runTest($testName, $testName::requests());

      if (! empty($this->postTest)) {
        $postTest = $this->getTest($this->postTest);
        $this->runTest($postTest . " {$count}", $postTest::requests());
        $count += 1;
      }
    }

    return $this;
  }

  /**
   *
   */
  public function summary()
  {
    if (! $this->list) {
      printf("\nSucceeded %d Failed %d\n\n",
             $this->runCount - count($this->failures),
             count($this->failures));

      if (! empty($this->failures)) {
        printf("\n%s\n\n", join(', ', $this->failures));
      }
    }

    return $this;
  }

  /**
   *
   */
  private function findTests()
  {
    $dir_iterator = new RecursiveDirectoryIterator(__DIR__. "/tests");
    $iterator = new RecursiveIteratorIterator($dir_iterator, RecursiveIteratorIterator::SELF_FIRST);
    // could use CHILD_FIRST if you so wish

    foreach ($iterator as $file) {
      if (preg_match('/Test.php$/u', $file->getBaseName())) {
        $this->testPaths[] = $file->getPathName();
      }
    }
  }

  /**
   *
   */
  public function runTest($testName, $requests)
  {
    $this->cleaningUp = false;
    $this->runCount += 1;
    $this->testName = $testName;
    $this->csv[] = [];
    $this->csv[] = [ $testName ];
    $this->csv[] = [ ];

    if (self::$debug) {
      printf("\n\n=====\nRUNNING TEST %s\n======\n\n", $testName);
    }

    $this->map = [];
    $this->cookies = [];
    $this->label = null;

    try {
      $requestCount = count($requests);

      foreach ($requests as $i => $request)  {
        $this->run_request($i, $request);
      }

      printf("TEST %s OK\n", $testName);
    } catch (TestException $e) {
      if (self::$debug) {
        // Include message in debug output
        printf("Test %s\nError\n%s\n", $testName, $e->getOutput());
      }

      if (! empty($this->csvFile)) {
        $n = count($this->csv);
        $this->csv[$n - 1][4] = $e->getMessage();
        $this->csv[$n - 1][6] = $e->getOutput();
      }

      $this->failures[] = $testName;

      fprintf(STDERR, "Test %s\nError\n%s\n", $testName, $e->getShort());
    } finally {
      if (method_exists($testName, 'cleanup')) {
        $this->cleaningUp = true;

        $cleanupRequests = $testName::cleanup();
        $this->label = null;

        foreach ($cleanupRequests as $j => $request) {
          try {
            $this->run_request($j + $requestCount, $request);
          } catch (TestException $e) {
            // Ignore cleanup errors
            if (self::$debug) {
              // Include message in debug output
              printf("Test %s\nError\n%s\n", $testName, $e->getOutput());
            }

            if (! empty($this->csvFile)) {
              $n = count($this->csv);
              $this->csv[$n - 1][4] = $e->getMessage();
            }
          }
        }
      }
    }

    return $this;
  }

  /**
   *
   */
  private function run_request($i, array $request)
  {
    $decode = null;
    $validate = [];
    $expect_error = null;

    list($patterns, $replacements) = $this->getSubstitutions($this->map);

    if (self::$debug) {
      printf("\n=== %d ======\n%s\n\n", $i, json_encode($request, JSON_PRETTY_PRINT));
    }

    $this->route = '';
    $this->output = '';
    $this->verb = '';
    $this->test_comment = '';

    if (count($request) < 1) {
      throw new TestException("Invalid request", '', print_r($request, true));
    }

    if (is_string($request[0]) && in_array($request[0], [ 'CONDITION', 'LABEL', 'SLEEP', 'GROUP' ])) {
      $arguments = $request;
    } else {
      if (is_string($request[0])) {
        $this->test_comment = $request[0];
        $request = array_slice($request, 1);
      }

      if (count($request) === 1) {
        $arguments = $request[0];
      } else if (count($request) === 2) {
        list($arguments, $decode) = $request;
      } else if (count($request) === 3) {
        list($arguments, $decode, $validate) = $request;
      } else if (count($request) === 4) {
        list($arguments, $decode, $validate, $expect_error) = $request;
      } else {
        throw new TestException("Invalid request", '', print_r($request, true));
      }
    }

    for ($i = 0; $i < 6; $i++) {
      $arguments[] = null;
    }

    list($verb, $route, $queryString, $postData, $formData, $fileName) = $arguments;

    if ($verb === 'GROUP') {
      foreach ($arguments[1] as $index => $command) {
        $this->run_request("{$i}.{$index}", $command);
      }
    } else if ($verb === 'LABEL') {
      if ($this->label === $route) {
        $this->label = null;
      }
    } else if ($this->label !== null) {
      // looking for label, skip this one
    } else if ($verb == 'SLEEP') {
      if (self::$debug) {
        printf("Sleeping for %d\n", $route);
      }

      sleep($route);
    } else if ($verb === 'CONDITION') {
      // Second parameter is an array of conditions
      // If all are true, skip to the label (third parameter)
      $conditionIsTrue = true;

      foreach ($arguments[1] as $var_name => $expected_expression) {
        list ($ok, $expected) = $this->evaluate_condition($var_name, $expected_expression, $patterns, $replacements);

        if (! $ok) {
          $conditionIsTrue = false;
          break;
        }
      }

      if ($conditionIsTrue) {
        $this->label = $queryString;
      }
    } else {
      $this->verb = $verb;

      if (self::$debug) {
        if (! empty($this->test_comment)) {
          printf("Test: %s\n", $this->test_comment);
        } else {
          printf("Test:\n");
        }
      }

      $data = $this->send($verb,
                          str_replace($patterns, $replacements, $route),
                          subst_object($queryString, $patterns, $replacements, $this->map),
                          subst_object($postData, $patterns, $replacements, $this->map),
                          subst_object($formData, $patterns, $replacements, $this->map),
                          $fileName);

      if (! empty($this->error)) {
        // There was an error response
        // Was it expected
        if (isset($expect_error)) {
          $object = $this->decode($data);

          if (is_array($expect_error)) {
            if (! isset($object[$expect_error['key']])) {
              throw new TestException("Missing error message: , expecting '{$expect_error['key']}'",
                                      $this->route, $this->output, $this->verb,
                                      $this->test_comment);
            } else if ($object[$expect_error['key']] != $expect_error['message']) {
              throw new TestException("Unexpected error message: '{$object[$expect_error['key']]}', expecting '{$expect_error['message']}'",
                                      $this->route, $this->output, $this->verb,
                                      $this->test_comment);
            }

          } else if (! isset($object['message'])) {
            throw new TestException("Missing error message: , expecting '{$expect_error}'",
                                    $this->route, $this->output, $this->verb,
                                    $this->test_comment);
          } else if ($object['message'] !== $expect_error) {
            throw new TestException("Unexpected error message: '{$object['message']}', expecting '{$expect_error}'",
                                    $this->route, $this->output, $this->verb,
                                    $this->test_comment);
          }
        } else {
          if (self::$debug) {
            fprintf(STDERR, "PROCESSING ERROR %s %s\n\n%s\n%s\n",
                    $verb, $route, $this->error, $this->output);
          }

          throw new TestException("ERROR OCCURRED", $this->route, $this->output, $this->verb, $this->test_comment);
        }
      } else {
        // Not error response
        // Was one expected? Then report
        if (isset($expect_error)) {
          $expected_message = json_encode($expect_error);

          throw new TestException("EXPECTED ERROR {$expected_message} did not occur",
                                  $this->route, $this->output, $this->verb, $this->test_comment);
        }

        // Asked to extract some fields from a json response
        // Error if response is not json
        if (! empty($decode)) {
          $object = $this->decode($data);

          foreach($decode as $var => $path) {
            $this->map[$var] = get_path($path, $object);
          }

          if (self::$debug) {
            printf("MAP\n");
            print_r($this->map);
          }
        }

        if (! empty($validate) && is_array($validate)) {
          list($patterns, $replacements) = $this->getSubstitutions();

          if (count(array_filter(array_keys($validate), 'is_string')) > 0) {
            foreach ($validate as $var_name => $expected_expression) {
              list ($ok, $expected, $testMessage) = $this->evaluate_condition($var_name, $expected_expression, $patterns, $replacements);

              if ($this->csvFile !== null) {
                $this->csv[] = [ '', '', '', sprintf("%s => %s", $var_name, isset($expected) ? $expected : '<>'), $ok ? 'Pass' : 'Fail' ];
              }

              if (! $ok) {
                if (empty($testMessage)) {
                throw new TestException(sprintf("INCORRECT VALUE '%s' FOR %s (expected '%s')",
                                                ! isset($this->map[$var_name]) ? '<null>' : $this->encode($this->map[$var_name]),
                                                $var_name,
                                                isset($expected) ? $expected : '<>'),
                                        $this->route,
                                        $this->output,
                                        $this->verb,
                                        $this->test_comment);
                } else {
                  throw new TestException(sprintf("%s '%s' FOR %s (expected '%s')",
                                                $testMessage,
                                                ! isset($this->map[$var_name]) ? '<null>' : $this->encode($this->map[$var_name]),
                                                $var_name,
                                                isset($expected) ? $expected : '<>'),
                                        $this->route,
                                        $this->output,
                                        $this->verb,
                                        $this->test_comment);
                }
              }
            }
          } else {
            foreach ($validate as $record) {
              list ($testMessage, $var_name, $expected_expression) = $record;

              list ($ok, $expected) = $this->evaluate_condition($var_name, $expected_expression, $patterns, $replacements);

              if ($this->csvFile !== null) {
                $this->csv[] = [ '', '', '', $testMessage, $ok ? 'Pass' : 'Fail' ];
              }

              if (! $ok) {
                throw new TestException(sprintf("%s '%s' FOR %s (expected '%s')",
                                                $testMessage,
                                                ! isset($this->map[$var_name]) ? '<null>' : $this->encode($this->map[$var_name]),
                                                $var_name,
                                                isset($expected) ? $expected : '<>'),
                                        $this->route,
                                        $this->output,
                                        $this->verb,
                                        $this->test_comment);
              }
            }
          }
        }
      }
    }
  }

  /**
   *
   */
  protected function getSubstitutions()
  {

    $patterns = [];
    $replacements = [];

    foreach ($this->map as $var => $value) {
      if (is_string($var)) {
        if (! is_null($value) && is_array($value)) {
          try {
            $encoded = json_encode($value, JSON_PRETTY_PRINT);
          } catch(Exception $e) {
            $encoded = (string) $value;
          }
        } else if ($value === null) {
          $encoded = $value;
        } else {
          $encoded = (string) $value;
        }

        $patterns[] = '${' . $var . '}';
        $replacements[] = $encoded;
      }
    }

    return [ $patterns, $replacements ];
  }

  /**
   * Analyze a boolean condition and evaluate to true or false
   * substitute in order to compute a value.
   * Recursively substitute in arrays.
   */
  protected function evaluate_condition($var_name, $expected_expression, $patterns, $replacements)
  {
    $ok = false;
    $testMessage = null;

    if (is_array($expected_expression)
        && count($expected_expression) === 2
        && count(array_filter(array_keys($expected_expression), 'is_string')) === 0) {
      // Not an associative array
      $testMessage = $expected_expression[0];
      $expected = $expected_expression[1];
      $expected_expression = $expected_expression[1];
    } else {
      $expected = $expected_expression;
    }

    if (is_null($expected_expression)) {
      $ok = ! isset($this->map[$var_name]) || $this->map[$var_name] === null;
      $expected = '@null';

    } else if (is_callable($expected_expression)) {
      $ok = $expected_expression(isset($this->map[$var_name]) ? $this->map[$var_name] : null, $this->map);
      $expected = 'fn()';

    } else if (is_array($expected)) {
      $ok = isset($this->map[$var_name]) && $this->checkEquals(subst_object($expected, $patterns,  $replacements, $this->map),
                                                               $this->map[$var_name]);
      $expected = json_encode($expected_expression);

    } else if ($expected_expression === '@null') {
      $ok = ! isset($this->map[$var_name]) || $this->map[$var_name] === null;

    } else if (strtolower($expected_expression) === '@notnull') {
      $ok = isset($this->map[$var_name]) && $this->map[$var_name] !== null;

    } else {
      $expected = str_replace($patterns, $replacements, $expected_expression);

      // for numbers compare < and > as float
      if (preg_match('/^>\s*([.\d]+)\s*$/ui',$expected, $matches)) {
        $ok = isset($this->map[$var_name]) && floatval($this->map[$var_name]) > floatval($matches[1]);

      } else if (preg_match('/^<\s*([.\d]+)\s*$/ui',$expected, $matches)) {
        $ok = isset($this->map[$var_name]) && floatval($this->map[$var_name]) < floatval($matches[1]);

      } else if (preg_match('/^>=\s*([.\d]+)\s*$/ui',$expected, $matches)) {
        $ok = isset($this->map[$var_name]) && floatval($this->map[$var_name]) >= floatval($matches[1]) - .005;

      } else if (preg_match('/^<=\s*([.\d]+)\s*$/ui',$expected, $matches)) {
        $ok = isset($this->map[$var_name]) && floatval($this->map[$var_name]) <= floatval($matches[1]) + .005;

      } else if (preg_match('/^>(.+)$/ui',$expected, $matches)) {
        $ok = isset($this->map[$var_name]) && $this->map[$var_name] > $matches[1];

      } else if (preg_match('/^<(.+)$/ui',$expected, $matches)) {
        $ok = isset($this->map[$var_name]) && $this->map[$var_name] < $matches[1];

      } else {
        $ok = isset($this->map[$var_name]) && $this->map[$var_name] == $expected;
      }
    }

    return [ $ok, $expected, $testMessage ];
  }

  /**
   * Recursive check for equality.
   *
   * Only compare array keys present in $expected,
   * ie extra keys may appear in value.
   */
  protected function checkEquals($expected, $value)
  {
    $result = false;

    if ($value === $expected) {
      $result = true;
    } else if (is_array($value)) {
      if (is_array($expected)) {
        $result = true;

        foreach ($expected as $key => $expectedItem) {
          if (! array_key_exists($key, $value)) {
            $result = false;
          } else {
            $result = $this->checkEquals($expectedItem, $value[$key]);
          }

          if (! $result) {
            break;
          }
        }
      }
    } else {
      $result = $value == $expected;
    }

    return $result;
  }

  /**
   *
   */
  protected function encode($data)
  {
    if (! isset($data)) {
      return '<empty>';
    } else if (is_string($data)) {
      return $data;
    } else {
      return json_encode($data, JSON_PRETTY_PRINT);
    }
  }

  /**
   *
   */
  protected function decode($data)
  {
    if (preg_match('/^\s*<http>/ui', $data)) {
      return $data;
    } else {
      try {
        $object = json_decode($data, true);

        if ($object === false || $object === null) {
          throw new TestException("NON-JSON RESPONSE", $this->route, $this->output, $this->verb);
        }
      } catch (Exception $e) {
        throw new TestException("NON-JSON RESPONSE " . $e->getMessage(), $this->route, $this->output, $this->verb);
      }

      return $object;
    }
  }

  /**
   *
   */
  public function printTimes()
  {
    if ($this->show_times) {
      printf("\n\nTIMES\n\n");

      foreach ($this->times as list($route, $verb, $time)) {
        printf("%-7s %-45s %12.1f\n", $verb, $route, $time * 1000);
      }
    }

    return $this;
  }

  /**
   *
   */
  public function dumpCSV()
  {
    if (! empty($this->csvFile)) {

      $h = fopen($this->csvFile, 'w');

      if ($h === false) {
        throw new Exception("Unable to open {$this->csvFile}\n");
      }

      fputcsv($h, [ "Test", "Description", 'Verb', 'URL', 'Error', 'Time(ms)' ]);

      foreach ($this->csv as $row) {
        fputcsv($h, $row);
      }

      // Add the histogram
      for ($i = 0; $i < 5; $i++) {
        fputcsv($h, []);
      }

      fputcsv($h, [ 'Histogram' ]);
      fputcsv($h, []);

      $samples = 0;

      foreach ($this->time_histogram as $count) {
        $samples += $count;
      }

      $number_of_buckets = count($this->time_histogram);
      $bucket_start = 0;
      $bucket_end = self::$bucket_size;
      $i = 0;
      $cumulative = 0;

      for (; $i < $number_of_buckets - 1; $i += 1) {
        $cumulative += 100.0 * $this->time_histogram[$i] / $samples;
        fputcsv($h, [
                     sprintf("%d - %d", $bucket_start, $bucket_end),
                     $this->time_histogram[$i],
                     sprintf("%0.1f", 100.0 * $this->time_histogram[$i] / $samples),
                     sprintf("%0.1f", $cumulative)
                     ]);

        $bucket_start += self::$bucket_size;
        $bucket_end += self::$bucket_size;
      }

      $cumulative += 100.0 * $this->time_histogram[$i] / $samples;

      fputcsv($h, [
                   sprintf("> %d", $bucket_start),
                   $this->time_histogram[$i],
                   sprintf("%0.1f", 100.0 * $this->time_histogram[$i] / $samples),
                   sprintf("%0.1f", $cumulative)
                   ]);

      fputcsv($h, []);
      fputcsv($h, [ 'Sorted times ']);
      fputcsv($h, []);

      $time_list = $this->times;
      usort($time_list, function($a, $b) {
          if ($a[2] < $b[2]) {
            return 1;
          } else if ($a[2] > $b[2]) {
            return -1;
          } else {
            return 0;
          }
        });

      $i = 20;

      foreach ($time_list as $item) {
        fputcsv($h, $item);

        $i--;

        if ($i < 1) {
          break;
        }
      }

      fclose($h);
    }

    return $this;
  }

  /**
   *
   */
  public function printAverages()
  {
    if ($this->show_times) {
      printf("\n");

      foreach ($this->average_times as $verb => $values) {
        if (empty($values)) {
           printf("%-10s %12.0f %5d %12.0f %12.0f\n", $verb, 0, 0, 0, 0);
        } else {
          printf("%-10s %12.0f %5d %12.0f %12.0f\n",
                 $verb,
                 reduce(function($value, $total) {
                     return $total + $value;
                   }, $values, 0) * 1000 / count($values),
                 count($values),
                 reduce(function($value, $min) {
                     return $min > $value ? $value : $min;
                   }, $values, 99999) * 1000,
                 reduce(function($value, $max) {
                     return $max < $value ? $value : $max;
                   }, $values, 0) * 1000);
        }
      }

      printf("\n");
    }

    return $this;
  }


  /**
   * Store a header read from the response to an API request
   * in the instance's header array.
   *
   * Extract cookie and error information
   *
   * @param $ch Curl request object
   * @param string $header - the header
   *
   * @return int length of header
   */
  public function _readHeader($ch, $header)
  {
    $this->headers[] = trim($header);

    if (self::$curl_debug) {
      printf("%s", $header);
    }

    $matches = [];

    if (preg_match("/^set-cookie:\s+([^\r]+)\r*$/i", $header, $matches)) {
      $cookie = $matches[1];

      if (preg_match('/^([^=]+)=/', $cookie, $matches)) {
        $name = $matches[1];
        $this->cookies[$name] = $cookie;
      }
    } else if (preg_match("/^WPError:\s+(.+)$/i", $header, $matches)) {
      $this->wpError = $matches[1];
    }

    return strlen($header);
  }


  /**
   *
   */
  public function contentType()
  {
    $contentType = '';

    foreach ($this->headers as $header) {
      if (preg_match('/content-type: (.*)$/ui', $header, $matches)) {
        $contentType = $matches[1];
      }
    }

    return $contentType;
  }


  /**
   *
   */
  public function contentTypeMatches($pattern)
  {
    return preg_match("/{$pattern}/ui", $this->contentType());
  }


  /**
   * Change the setting of verifyPeer in the instance.
   *
   * If verifyPeer is false, the requests to the server do not
   * check ssl certificates.
   *
   * @param boolean $value
   *
   * @return void
   */
  public function setVerifyPeer($value)
  {
    $this->verifyPeer = $value;
  }

  /**
   * Send the curl request to the API.
   */
  protected function post($route, array $queryString = [], array $postData = null, array $formData = null, $fileName = null)
  {
    $this->send('POST', $route, $queryString, $postData, $formData, $fileName);
  }

  /**
   * Send the curl request to the API.
   */
  protected function get($route, array $queryString = [], array $postData = null, array $formData = null, $fileName = null)
  {
    $this->send('GET', $route, $queryString, $postData, $formData, $fileName);
  }

  /**
   * Send the curl request to the API.
   */
  protected function send($verb, $route, array $queryString = null, $postData = null, array $formData = null, $fileName = null)
  {
    $this->error = null;
    $this->info = null;
    $this->verb = $verb;

    if ($this->cleaningUp && preg_match('/\${/ui', $route)) {
      throw new TestException("INCOMPLETE ROUTE CLEANING UP");
    }

    if (preg_match('/^https?:/ui', $this->host)) {
      // Assume complete host
      $hostString = $this->host;
    } else if (preg_match('/:\d+$/ui', $this->host)) {
      // Host has port
      $hostString = "http://{$this->host}";
    } else {
      $hostString = "http://{$this->host}:8080";
    }

    $route = str_replace(' ', '%20', $route);

    if (! empty($queryString)) {
      $url = "{$hostString}{$route}?" . http_build_query($queryString);
    } else {
      $url = "{$hostString}{$route}";
    }

    if (self::$debug || self::$header_debug) {
      printf("RUNNING %s %s\n", $verb, $url);
    }

    $this->route = $url;

    $ch = curl_init($url);

    curl_setopt($ch, CURLOPT_HEADER, 0);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $verb);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HEADERFUNCTION, array($this, '_readHeader'));
    curl_setopt($ch, CURLOPT_TIMEOUT, 0);
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 0);
    curl_setopt($ch, CURLOPT_ENCODING , "");
    curl_setopt($ch, CURLOPT_VERBOSE, $this->curlVerbose);

    if (! isset($postData) && ! isset($formData) && preg_match('/^(post|put)$/ui', $verb)) {
      $postData = [ 'dummy' => 'dummy' ];
    }

    if (! isset($postData) && is_array($formData)) {
      curl_setopt($ch, CURLOPT_POST, true);
      $multiPart = [];

      foreach ($formData as $key => $value) {
        if (substr($value, 0, 1) === '@') {
          $multiPart[$key] = curl_file_create(substr($value, 1));
        } else {
          $multiPart[$key] = $value;
        }
      }

      if (self::$debug || self::$header_debug) {
        print_r($formData);
        printf("\n");
      }

      curl_setopt($ch, CURLOPT_POSTFIELDS, $multiPart);
    } else if (is_string($postData)) {
      if (self::$debug || self::$header_debug) {
        printf("%s\n", $postData);
      }

      curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
    } else {
      if (self::$debug || self::$header_debug) {
        printf("%s\n", json_encode($postData, JSON_PRETTY_PRINT));
      }

      curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($postData));
    }

    if (! $this->verifyPeer) {
      curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    }

    if (count($this->cookies) > 0) {
      curl_setopt($ch, CURLOPT_COOKIE, implode(',', $this->cookies));
    }

    $send_headers = array('request-source: document-manager-session');

    if (! empty($postData)) {
      $send_headers[] = "content-type: application/json";
    }

    curl_setopt($ch, CURLOPT_HTTPHEADER, $send_headers);


    $this->headers = [];

    if ($fileName !== null) {
      $h = fopen($fileName, 'w');

      if ($h === false) {
        throw new TestException("Unable to open file '{$fileName}'", $this->route, error_get_last());
      }

      curl_setopt($ch, CURLOPT_FILE, $h);
      $start_time = microtime(true);
      $status = curl_exec($ch);
      $end_time = microtime(true);

      $this->output = '';

      if ($status === false) {
        $this->error = curl_error($ch);
      }

      fclose($h);
    } else {
      $start_time = microtime(true);
      $this->output = curl_exec($ch);
      $end_time = microtime(true);
      $this->info = curl_getinfo($ch);

      if ($this->output === false) {
        $this->error = curl_error($ch);
      } else if (! preg_match('/^[32]/', $this->info['http_code'])) {
        $this->error = $this->info['http_code'];
      }

      if ($this->contentTypeMatches('image')) {
        $this->output = "\n" . $this->contentType() . "\n";
      }

      if (self::$header_debug) {
        foreach ($this->headers as $header) {
          printf("%s\n", $header);
        }
      }

      if (self::$debug) {
        if ($this->output !== false) {
          $print = $this->output;

          try {
            $json = json_decode($this->output);

            if ($json !== false && $json !== null) {
              $print = json_encode($json, JSON_PRETTY_PRINT);
            }
          } catch (Exception $e) {

          }

          echo("RESPONSE\n" . $print) . "\n---\n";
        }

        if (! empty($this->error)) {
          echo("ERROR\n" . $this->error) . "\n---\n";
        }
      }
    }

    if ($this->csvFile !== null) {
      $this->csv[] = [ $this->testName, $this->test_comment, $verb, str_replace($hostString, '', $url), '', sprintf("%0.2f", 1000 * ($end_time - $start_time)), '' ];
    }

    $this->times[] = [ $route, $verb, $end_time - $start_time ];
    $this->average_times[$verb][] = $end_time - $start_time;
    $this->update_histogram($end_time - $start_time);

    curl_close($ch);

    return $this->output;
  }

  /**
   * Count number of times in each bucket
   */
  protected function update_histogram($time)
  {
    $bucket = floor($time * 1000 / self::$bucket_size);

    if ($bucket > self::$max_buckets) {
      $bucket = self::$max_buckets;
    }

    for ($i = count($this->time_histogram); $i < $bucket + 1; $i += 1) {
      $this->time_histogram[$i] = 0;
    }

    $this->time_histogram[$bucket] += 1;
  }
}

function all(callable $fn, $list)
{
  if (is_array($list) || $list instanceof Traversable || $list instanceof stdClass) {
    $all = true;

    foreach ($list as $item) {
      if (! $fn($item)) {
        $all = false;
        break;
      }
    }
  } else {
    $all = false;
  }

  return $all;
}

function some(callable $fn, $list)
{
  if (is_array($list) || $list instanceof Traversable || $list instanceof stdClass) {
    $some = false;

    foreach ($list as $item) {
      if ($fn($item)) {
        $some = true;
        break;
      }
    }
  } else {
    $some = false;
  }

  return $some;
}

function find(callable $fn, $list)
{
  if (is_array($list) || $list instanceof Traversable || $list instanceof stdClass) {
    $some = null;

    foreach ($list as $item) {
      if ($fn($item)) {
        $some = $item;
        break;
      }
    }
  } else {
    $some = null;
  }

  return $some;
}

function filter(callable $fn, $list)
{
  if (is_array($list) || $list instanceof Traversable || $list instanceof stdClass) {
    $some = [];

    foreach ($list as $item) {
      if ($fn($item)) {
        $some[] = $item;
      }
    }
  } else {
    $some = [];
  }

  return $some;
}

function reduce(callable $fn, $list, $start = [])
{
  if (is_array($list) || $list instanceof Traversable || $list instanceof stdClass) {
    $result = $start;

    foreach ($list as $item) {
      $result = $fn($item, $result);
    }
  } else {
    $result = false;
  }

  return $result;
}

function findRoleId($object, $pattern)
{
  $roleId = null;

  foreach ($object['authorities'] as $authority) {
    if (isset($authority['roleName'])
        && strpos($authority['roleName'], $pattern) !== false) {
      $roleId = $authority['id'];
      break;
    }
  }

  return $roleId;
}