#!/bin/bash

(
  cd resources/test/js/goog
  patch
) < ./script/base.js.patch
