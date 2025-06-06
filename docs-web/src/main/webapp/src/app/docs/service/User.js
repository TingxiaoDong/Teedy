'use strict';

/**
 * User service.
 */
angular.module('docs').factory('User', function(Restangular) {
  var userInfo = null;
  
  return {
    /**
     * Returns user info.
     * @param force If true, force reloading data
     */
    userInfo: function(force) {
      if (userInfo === null || force) {
        userInfo = Restangular.one('user').get();
      }
      return userInfo;
    },
    
    /**
     * Login an user.
     */
    login: function(user) {
      return Restangular.one('user').post('login', user, {}, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        transformRequest: function(obj) {
          var str = [];
          for(var p in obj) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
          }
          return str.join("&");
        }
      });
    },
    
    /**
     * Logout the current user.
     */
    logout: function() {
      return Restangular.one('user').post('logout', {});
    }
  }
});