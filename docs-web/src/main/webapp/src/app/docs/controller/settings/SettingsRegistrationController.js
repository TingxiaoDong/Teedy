'use strict';

/**
 * 注册申请管理页面控制器
 */
angular.module('docs').controller('SettingsRegistrationController', ['$scope', 'Restangular', '$uibModal', '$translate', function($scope, Restangular, $uibModal, $translate) {
  // 加载待处理的注册申请
  function loadPendingRequests() {
    Restangular.one('register-request').get({
      status: 'PENDING'
    }).then(function(response) {
      $scope.pendingRequests = response;
      console.log('Pending requests:', response);
      if (response && response.length > 0) {
        console.log('First request password:', response[0].password);
      }
    });
  }

  // 加载已处理的注册申请
  function loadProcessedRequests() {
    Restangular.one('register-request').get({
      status: 'PROCESSED'
    }).then(function(response) {
      $scope.processedRequests = response;
    });
  }

  // 接受注册申请
  $scope.acceptRequest = function(request) {
    var modalInstance = $uibModal.open({
      templateUrl: 'partial/docs/registration.accept.html',
      controller: 'RegistrationAcceptController',
      resolve: {
        request: function() {
          return request;
        }
      }
    });

    modalInstance.result.then(function(adminRemark) {
      console.log('Sending accept request for:', request.id, 'with remark:', adminRemark);
      console.log('Request object:', request);
      
      var data = {
        adminRemark: adminRemark
      };
      
      console.log('Sending data:', data);
      console.log('Request URL:', '/api/register-request/' + request.id + '/accept');
      
      Restangular.one('register-request', request.id).customPOST($.param(data), 'accept', {}, {
        'Content-Type': 'application/x-www-form-urlencoded'
      }).then(function(response) {
        console.log('Request accepted successfully, response:', response);
        loadPendingRequests();
        loadProcessedRequests();
      }, function(error) {
        console.error('Error accepting request:', error);
        console.error('Error status:', error.status);
        console.error('Error data:', error.data);
        $uibModal.open({
          templateUrl: 'partial/docs/error.html',
          controller: 'ErrorController',
          resolve: {
            errorMessage: function() {
              return error.data || 'registration.accept_error';
            }
          }
        });
      });
    });
  };

  // 拒绝注册申请
  $scope.rejectRequest = function(request) {
    var modalInstance = $uibModal.open({
      templateUrl: 'partial/docs/registration.reject.html',
      controller: 'RegistrationRejectController',
      resolve: {
        request: function() {
          return request;
        }
      }
    });

    modalInstance.result.then(function(adminRemark) {
      var formData = new FormData();
      formData.append('adminRemark', adminRemark);
      
      Restangular.one('register-request', request.id).customPOST(formData, 'reject', {}, {
        'Content-Type': 'application/x-www-form-urlencoded',
        'transformRequest': function(data) {
          return $.param(data);
        }
      }).then(function() {
        loadPendingRequests();
        loadProcessedRequests();
      }, function(error) {
        console.error('Error rejecting request:', error);
        $uibModal.open({
          templateUrl: 'partial/docs/error.html',
          controller: 'ErrorController',
          resolve: {
            errorMessage: function() {
              return error.data || 'registration.reject_error';
            }
          }
        });
      });
    });
  };

  // 初始加载数据
  loadPendingRequests();
  loadProcessedRequests();
}]); 