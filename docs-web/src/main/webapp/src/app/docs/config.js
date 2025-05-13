.state('settings.registration', {
  url: '/registration',
  templateUrl: 'partial/docs/settings.registration.html',
  controller: 'SettingsRegistrationController',
  resolve: {
    userInfo: function(UserService) {
      return UserService.getUserInfo();
    }
  }
}) 