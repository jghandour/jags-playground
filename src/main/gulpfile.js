"use strict";

// Imports
var gulp = require("gulp");
var concat = require("gulp-concat");
var del = require("del");
var uglify = require("gulp-uglify");
var uglifycss = require("gulp-uglifycss");
var sourcemaps = require("gulp-sourcemaps");
var ncu = require("npm-check-updates");
var appcache = require("gulp-appcache");

// Variables
var app_manifest_file = "app.manifest";
var path_destination = "webapp/";
var path_app_source = "frontend/";
var path_lib_source = "node_modules/";
var src_bundle_lib_js = [
    path_lib_source + "jquery/dist/jquery.min.js",
    path_lib_source + "angular/angular.js",
    path_lib_source + "angular-animate/angular-animate.js",
    path_lib_source + "angular-route/angular-route.js",
    path_lib_source + "angular-loading-bar/src/loading-bar.js",
    path_lib_source + "bootstrap/dist/js/bootstrap.min.js",
    path_lib_source + "angular-ui-bootstrap/dist/ui-bootstrap.js"
];
var src_bundle_lib_css = [
    path_lib_source + "bootstrap/dist/css/bootstrap.min.css",
    path_lib_source + "angular-loading-bar/src/loading-bar.css"
];
var src_bundle_app_js = [
    path_app_source + "**/app.module.js",
    path_app_source + "**/*.js"
];
var src_bundle_app_css = [
    path_app_source + "**/*.css"
];
var destination_files = [
    path_destination + "**/*",
    "!" + path_destination + "WEB-INF",
    "!" + path_destination + "WEB-INF/web.xml"
];

// Functions
function mergeJs (sourcePaths, bundleName) {
    return gulp.src(sourcePaths).pipe(sourcemaps.init()).pipe(concat(bundleName)).pipe(uglify()).pipe(sourcemaps.write("maps")).pipe(gulp.dest(path_destination));
}

function mergeCss (sourcePaths, bundleName) {
    return gulp.src(sourcePaths).pipe(sourcemaps.init()).pipe(concat(bundleName)).pipe(uglifycss()).pipe(sourcemaps.write("maps")).pipe(gulp.dest(path_destination));
}

// Tasks
gulp.task("copy", function () {
    var sources = [path_app_source + "**/*.js", path_app_source + "**/*.css", path_app_source + "**/*.html", path_app_source + "**/*.manifest"];
    return gulp.src(sources).pipe(gulp.dest(path_destination));
});

gulp.task("manifest", function () {
    gulp.src(destination_files).pipe(appcache({
            hash: true,
            preferOnline: true,
            network: ["http://*", "*"],
            filename: app_manifest_file,
            exclude: app_manifest_file
        }))
    .pipe(gulp.dest(path_destination));

    gulp.src(path_app_source + app_manifest_file).pipe(gulp.dest(path_destination));
});

gulp.task("lib", function () {
    mergeJs(src_bundle_lib_js, "lib.js");
    mergeCss(src_bundle_lib_css, "lib.css");
});

gulp.task("app", function () {
    mergeJs(src_bundle_app_js, "app.js");
    mergeCss(src_bundle_app_css, "app.css");
});

gulp.task("clean", function() {
    return del.sync(destination_files);
});

gulp.task("default", ["clean", "copy", "lib", "app", "manifest"]);

gulp.task("watch", function() {
    gulp.watch(path_app_source + "**/*", ["copy", "app", "manifest"]);
});

gulp.task("ncu", function() {
    ncu.run({
        packageFile: "package.json"
    }).then(function(upgradeable) {
        console.log("Dependencies to upgrade:", upgradeable);
    });
});
