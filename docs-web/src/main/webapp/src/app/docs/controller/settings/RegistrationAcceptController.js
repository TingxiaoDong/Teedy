'use strict';

angular.module('docs').controller('RegistrationAcceptController', ['$scope', '$uibModalInstance', 'request', function($scope, $uibModalInstance, request) {
  $scope.adminRemark = '';

  $scope.ok = function() {
    $uibModalInstance.close($scope.adminRemark);
  };

  $scope.cancel = function() {
    $uibModalInstance.dismiss('cancel');
  };
}]); 