<?php

class TestException extends Exception
{
  private $output = '';
  private $route = '';
  private $verb = '';
  private $test_comment = '';

  /**
   *
   */
  public function __construct($message, $route = '', $output = '', $verb = '',
                              $test_comment = '')
  {
    parent::__construct($message);
    $this->output = $output;
    $this->verb = $verb;
    $this->route = $route;
    $this->test_comment = $test_comment;
  }

  /**
   *
   */
  public function getShort()
  {
    $output = $this->output;

    try {
      $json = json_decode($this->output);

      if ($json !== null && $json !== false) {
        $output = json_encode($json, JSON_PRETTY_PRINT);
      }
    } catch (Exception $e) {

    }

    return sprintf("%s%s %s %s",
                   empty($this->test_comment) ? '' : $this->test_comment . ': ',
                   $this->verb, $this->route, $this->getMessage());
  }

  /**
   *
   */
  public function getOutput()
  {
    $output = $this->output;

    try {
      $json = json_decode($this->output);

      if ($json !== null && $json !== false) {
        $output = json_encode($json, JSON_PRETTY_PRINT);
      }
    } catch (Exception $e) {

    }

    return sprintf("%s%s %s\n%s\n\n%s\n",
                   empty($this->test_comment) ? '' : $this->test_comment . "\n",
                   $this->verb, $this->route, $this->getMessage(), $output);
  }
}