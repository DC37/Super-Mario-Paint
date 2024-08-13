@echo off
title Super Mario Paint v1.3.2
java --add-modules=ALL-MODULE-PATH -p "./jfx11/lib/javafx.base.jar;./jfx11/lib/javafx.controls.jar;./jfx11/lib/javafx.fxml.jar;./jfx11/lib/javafx.graphics.jar;./jfx11/lib/javafx.media.jar" -jar ./SMPv1.3.2_J9.jar
echo [Done]
pause