angular.module('docs').controller('RegisterController', function($scope, $http, $translate, $modalInstance) {
  $scope.request = {
    username: '',
    email: '',
    password: '',
    requestMessage: ''
  };
  $scope.emailError = false;

  $scope.close = function(request) {
    if (!request) {
      $modalInstance.dismiss('cancel');
      return;
    }

    $http.post('api/register-request', request)
      .success(function() {
        $modalInstance.close(request);
      })
      .error(function(data) {
        if (data && data.message === '该邮箱地址已被注册') {
          $scope.emailError = true;
        } else {
          $scope.error = data.message;
        }
      });
  };
}); 