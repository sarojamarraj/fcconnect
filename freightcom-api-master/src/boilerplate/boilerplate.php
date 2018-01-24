<?php


/**
 * Generate boilerplate for object controller
 */

if ($argc !== 3) {
  fprintf(STDERR, "Usage %s <object-name> <output-directory>\n", basename($argv[0]));
  exit();
}

run($argv[1], $argv[2]);

exit();


function run($objectName, $outputDirectory)
{
  list ($rootPath, $templates) = findTemplates();

  if (preg_match('@^(.*)/+$@ui', $outputDirectory, $matches)) {
    $outputDirectory = $matches[1];
  }

  $error = false;

  foreach ($templates as $template) {
    $path = $outputDirectory
      . str_replace($rootPath, '', preg_replace('/XTX/u', $objectName, $template));

    if (file_exists($path)) {
      $error = true;
      printf("File exists %s\n", $path);
    }
  }

  if (false) {
    printf("\nNothing generated\n");
    exit(-1);
  }

  foreach ($templates as $template) {
    $path = $outputDirectory
      . str_replace($rootPath, '', preg_replace('/XTX/u', $objectName, $template));

    if (! is_dir(dirname($path))) {
      $made = mkdir(dirname($path), 0700, true);

      if ($made === false) {
        throw new Exception("Cannot make directory "
                            . dirname($path)
                            . ' '
                            . error_get_last());
      }
    }

    $snakeCase = substr(preg_replace_callback('/([A-Z])/',
                                              function($matches) {
                                                return '_' . strtolower($matches[1]);
                                              },
                                              $objectName), 1);
    printf("%s %s\n", $path, $snakeCase);

    file_put_contents($path,
                      str_replace('xtx',
                                  lcfirst($objectName),
                                  str_replace('xtxp',
                                              $snakeCase,
                                              str_replace('XTX',
                                                          $objectName,
                                                          file_get_contents($template)
                                                          )
                                              )
                                  )
                      );
  }
}


function findTemplates()
{
  $templates = [];
  $root = new SPLFileInfo(__DIR__. "/templates");
  $rootPath = $root->getPathName();
  $dir_iterator = new RecursiveDirectoryIterator($root);
  $iterator = new RecursiveIteratorIterator($dir_iterator, RecursiveIteratorIterator::SELF_FIRST);
  // could use CHILD_FIRST if you so wish

  foreach ($iterator as $file) {
    if (preg_match('/\.java$/u', $file->getBaseName())) {
      $templates[] = $file->getPathName();
    }
  }

  return [ $rootPath, $templates];
}