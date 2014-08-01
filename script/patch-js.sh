#!/bin/bash

(
  cd resources/test/js/goog
  patch
) < ./script/base.js.patch

# (
#   cd resources/test/lib
#   patch
# ) < ./script/react.js.patch
