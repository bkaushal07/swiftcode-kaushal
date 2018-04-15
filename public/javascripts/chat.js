var app = angular.module('chatApp', ['ngMaterial']);

app.config(function ($mdThemingProvider) {
    $mdThemingProvider.theme('default')
        .primaryPalette('grey')
        .accentPalette('green');
});

app.controller('chatController', function ($scope) {
    $scope.messages = [
        {
            'sender': 'USER',
            'text': 'Hi'
    },
        {
            'sender': 'BOT',
            'text': 'Hello whats up?'
    },
        {
            'sender': 'USER',
            'text': 'IPL'
    },
        {
            'sender': 'BOT',
            'text': 'ESCN'
    }
    ];
});