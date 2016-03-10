// Routes
app.config(["$routeProvider", function ($routeProvider) {
    console.log("Initializing Sudoku Routes...");

    $routeProvider
        .when("/sudoku", {templateUrl: "app/sudoku/sudoku.html"});

}]);

// Controller
app.controller("SudokuCtrl", ["ApiService", "$scope", function (ApiService, $scope) {

    console.log("Initializing Sudoku Controller...");

    $scope.algorithm = "ANY";
    $scope.algorithms = [];

    $scope.difficultyLevel = null;
    $scope.difficultyLevels = [];

    $scope.getAlgorithms = function (algorithm, board) {
        ApiService.doGet("/api/v1/sudoku/algorithms", null, function (data) {
            if (data.status == 200) {
                $scope.algorithms = data.result;
                $scope.algorithms.unshift("ANY");
            }
        });
    }
    $scope.getAlgorithms();

    $scope.getDifficultyLevels = function (algorithm, board) {
        ApiService.doGet("/api/v1/sudoku/difficultyLevels", null, function (data) {
            if (data.status == 200) {
                $scope.difficultyLevels = data.result;
                $scope.difficultyLevel = $scope.difficultyLevels[2];
            }
        });
    }
    $scope.getDifficultyLevels();

    $scope.random = function (difficultyLevel) {
        $scope.sudokuBoard = null;
        ApiService.doGet("/api/v1/sudoku/random", {difficultyLevel: difficultyLevel}, function (data) {
            if (data.status == 200) {
                $scope.board = data.result;
            }
        });
    }

    $scope.getRandom = function () {
        $scope.random($scope.difficultyLevel);
    }

    $scope.solve = function (algorithm, board) {
        $scope.solvedBoard = null;
        $scope.solvedTime = null;
        $scope.solvedAlgorithm = null;
        $scope.solvedDifficultyLevel = null;
        ApiService.doGet("/api/v1/sudoku/solve", {algorithm: algorithm, board: board}, function (data) {
            if (data.status == 200) {
                $scope.solvedBoard = data.result.board;
                $scope.solvedTime = data.result.time;
                $scope.solvedAlgorithm = data.result.algorithm;
                $scope.solvedDifficultyLevel = data.result.difficultyLevel;
            }
        });
    }

    $scope.getSolution = function () {
        $scope.solve($scope.algorithm, $scope.board);
    }

    $scope.isBoardValid = function () {
        return $scope.board != null && $scope.board.length == 81;
    }

}]);

// Directive - Sudoku Board
app.directive("jagsSudokuBoard", function() {
    return {
        templateUrl: "/app/sudoku/sudokuBoard.html",
        scope: {
            board: "=board"
        }
    }
});