<?php

/**
 * Input is from /api/mappings
 *
 * E.g. ~/systems/spring-projects/project1/src/test/resources/test-route $HOST '/api/mappings' > /tmp/1
 *
 * Need to strip header first.
 */

extract_routes($argv);

exit();

function extract_routes($argv)
{
  $mappings = json_decode(file_get_contents('php://stdin'), true);

  $route_comments = read_info($argv);
  $seen = [];
  $not_seen = [];

  echo "<table style=\"border-collapse:collapse;border: 0px solid black;\">\n";
  $count = 0;

  foreach ($mappings as $route => $info) {
    if (preg_match('@\{\[(.+)\],methods=\[([^\]]+)\](,produces=\[([^\]]+)\])?@ui', $route, $matches)) {

      $key = "{$matches[2]},{$matches[1]}";

      if (isset($route_comments[$key])) {
        $comments = $route_comments[$key];
        $seen[$key] = 1;
      } else {
        $comments = '';
        $not_seen[$key] = 1;
      }

      printf("<tr style=\"\"><td  style=\"font-weight: bold; border: 0 solid black;\">%s</td><td  style=\"border: 0 solid black;\">%s</td><td  style=\"border: 0 solid black;\">%s</td></tr><tr><td colspan=\"3\" style=\"border: 0 solid black;\">%s<br></td></tr><tr><td colspan=\"3\">&nbsp;</td></tr>\n",
              htmlentities($matches[1]), htmlentities($matches[2]), empty($matches[4]) ? '' : htmlentities($matches[4]), $comments);

      $count++;
    }
  }

  echo "</table>\n";

  fprintf(STDERR, "%d end points\n", $count);

  foreach ($route_comments as $key => $comment) {
    if (! isset($seen[$key])) {
      fprintf(STDERR, "Missed %s\n", $key);
    }
  }

  foreach ($not_seen as $key => $set) {
    fprintf(STDERR, "NO COMMENT FOR  %s\n", $key);
  }
}

function read_info($argv)
{
  $info = [];

  if (count($argv) > 1) {

    if (! file_exists($argv[1])) {
      $file = __DIR__ . '/' . $argv[1];
    } else {
      $file = $argv[1];
    }

    if (! file_exists($file)) {
      throw new Exception("Can't find info {$file}");
    }

    $data = file_get_contents($file);
    $key = '';
    $text = '';

    foreach (explode("\n", $data) as $line) {
      if (preg_match('/^(GET|PUT|DELETE|POST|HEAD|GET.*HEAD),/u', $line)) {
        if (! empty($key)) {
          $info[$key] = $text;
        }

        $key = $line;
        $text = '';
      } else {
        $text .= $line . "\n";
      }
    }
  }

  return $info;
}