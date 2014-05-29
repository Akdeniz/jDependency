jDependency
===========

Static dependency resolver for java modules.

This project outputs a sparse matrix in html format that represents dependent classes from one module to another.

This is a one night project so don't expect much! 


Usage
----

    usage: jdependency [-h] -d DIRECTORY [-o [OUTPUT]]
                       [-i [INCLUDE [INCLUDE ...]]]
                       [-e [EXCLUDE [EXCLUDE ...]]]
    
    Static dependency resolver for java modules.
    
    optional arguments:
      -h, --help             show this help message and exit
      -d DIRECTORY, --directory DIRECTORY
                             Directory that modules will be searched in!
      -o [OUTPUT], --output [OUTPUT]
                             Output html file.
      -i [INCLUDE [INCLUDE ...]], --include [INCLUDE [INCLUDE ...]]
                             Class name patterns to include.
      -e [EXCLUDE [EXCLUDE ...]], --exclude [EXCLUDE [EXCLUDE ...]]
                             Class name patterns to exclude.


![Screenshot](http://akdeniz.github.io/images/jdependency.png)

License
----
    The MIT License (MIT)
    
    Copyright (c) 2014 Akdeniz
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
    
