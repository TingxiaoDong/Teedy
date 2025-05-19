'use strict';

/**
 * Admin user activity controller.
 */
angular.module('docs').controller('AdminUserActivityController', function($scope, Restangular) {
    // Load users
    $scope.loadUsers = function() {
        Restangular.one('admin/user/activity').get().then(function(response) {
            $scope.users = response.users;
        });
    };
    
    // Load users
    $scope.loadUsers();
}); 