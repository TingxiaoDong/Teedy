angular.module('docs').controller('ModalRegister', function($scope, $uibModalInstance, Restangular, $dialog, $translate) {
  $scope.request = {
    username: '',
    email: '',
    password: '',
    requestMessage: ''
  };

  $scope.close = function(request) {
    if (request === null) {
      $uibModalInstance.dismiss();
      return;
    }

    // 提交注册请求，使用表单格式
    Restangular.one('register-request').customPOST(request, '', {}, {
      'Content-Type': 'application/x-www-form-urlencoded'
    }).then(function(response) {
      var title = $translate.instant('register.success_title');
      var msg = $translate.instant('register.success_message');
      var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
      $dialog.messageBox(title, msg, btns);
      $uibModalInstance.close();
    }, function(response) {
      var title = $translate.instant('register.error_title');
      var msg = $translate.instant('register.error_message');
      var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
      $dialog.messageBox(title, msg, btns);
    });
  };
}); 