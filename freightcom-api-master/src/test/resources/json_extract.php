<?php


if ($argc === 3) {
  $file = $argv[1];
  $pattern = $argv[2];
 } else {
  $file = "php://stdin";
  $pattern = $argv[1];
 }

$map = json_decode(file_get_contents($file), true);

if ($map === false || $map === null) {
  print_r(json_last_error_msg());
  exit(-3);
}

get($pattern, $map);

exit();


function get($path, $map)
{
  $result = $map;

  foreach (explode('.', $path) as $component) {
    if (isset($result[$component])) {
      $result = $result[$component];
    } else {
      $result = null;
      break;
    }
  }

  if ($result === null) {
    printf("\n");
  } else if (is_array($result)) {
    printf("%s\n", json_encode($result));
  } else {
    printf("%s\n", $result);
  }
}