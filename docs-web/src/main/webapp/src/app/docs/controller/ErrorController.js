'use strict';

angular.module('docs').controller('ErrorController', ['$scope', '$uibModalInstance', 'errorMessage', function($scope, $uibModalInstance, errorMessage) {
  $scope.errorMessage = errorMessage;

  $scope.ok = function() {
    $uibModalInstance.close();
  };
}]); 