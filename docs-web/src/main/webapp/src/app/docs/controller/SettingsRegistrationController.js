angular.module('docs').controller('SettingsRegistrationController', function($scope, Restangular, $dialog, $translate) {
  // 加载待处理的注册申请
  function loadPendingRequests() {
    console.log('Loading pending requests...');
    Restangular.one('register-request').get({
      status: 'PENDING'
    }).then(function(response) {
      console.log('Pending requests response:', response);
      console.log('Pending requests data:', response.data);
      $scope.pendingRequests = response.data;
      console.log('Scope pending requests:', $scope.pendingRequests);
    }, function(error) {
      console.error('Error loading pending requests:', error);
      console.error('Error details:', error.data);
      $dialog.messageBox(
        $translate.instant('error'),
        $translate.instant('registration.load_error'),
        [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}]
      );
    });
  }

  // 加载已处理的注册申请
  function loadProcessedRequests() {
    console.log('Loading processed requests...');
    Restangular.one('register-request').get({
      status: 'PROCESSED'
    }).then(function(response) {
      console.log('Processed requests response:', response);
      console.log('Processed requests data:', response.data);
      $scope.processedRequests = response.data;
      console.log('Scope processed requests:', $scope.processedRequests);
    }, function(error) {
      console.error('Error loading processed requests:', error);
      console.error('Error details:', error.data);
      $dialog.messageBox(
        $translate.instant('error'),
        $translate.instant('registration.load_error'),
        [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}]
      );
    });
  }

  // 接受注册申请
  $scope.acceptRequest = function(request) {
    var modalInstance = $dialog.prompt({
      title: $translate.instant('settings.registration.accept_title'),
      message: $translate.instant('settings.registration.accept_message'),
      input: '',
      ok: $translate.instant('ok'),
      cancel: $translate.instant('cancel')
    });

    modalInstance.result.then(function(adminRemark) {
      console.log('Accepting request:', request);
      Restangular.one('register-request', request.id).customPOST({
        adminRemark: adminRemark
      }, 'accept').then(function() {
        loadPendingRequests();
        loadProcessedRequests();
      }, function(error) {
        console.error('Error accepting request:', error);
        console.error('Error details:', error.data);
        $dialog.messageBox(
          $translate.instant('error'),
          $translate.instant('registration.accept_error'),
          [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}]
        );
      });
    });
  };

  // 拒绝注册申请
  $scope.rejectRequest = function(request) {
    var modalInstance = $dialog.prompt({
      title: $translate.instant('settings.registration.reject_title'),
      message: $translate.instant('settings.registration.reject_message'),
      input: '',
      ok: $translate.instant('ok'),
      cancel: $translate.instant('cancel')
    });

    modalInstance.result.then(function(adminRemark) {
      console.log('Rejecting request:', request);
      Restangular.one('register-request', request.id).customPOST({
        adminRemark: adminRemark
      }, 'reject').then(function() {
        loadPendingRequests();
        loadProcessedRequests();
      }, function(error) {
        console.error('Error rejecting request:', error);
        console.error('Error details:', error.data);
        $dialog.messageBox(
          $translate.instant('error'),
          $translate.instant('registration.reject_error'),
          [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}]
        );
      });
    });
  };

  // 初始加载数据
  console.log('Initializing registration controller...');
  $scope.pendingRequests = [];
  $scope.processedRequests = [];
  loadPendingRequests();
  loadProcessedRequests();
}); 