angular.module('myApp', ['angucomplete-alt', 'ngCookies'])
.controller('Hello', ['$scope', '$http', '$window', '$cookies', function($scope, $http, $window, $cookies) {
	
	$scope.nomeUsuario = $cookies.get('name');
	veriricarSeEstaLogado($scope, $http, $window, $cookies);
	$scope.sair = function() {
		$cookies.put('authorizationToken', '', {path: '/'});
		veriricarSeEstaLogado($scope, $http, $window, $cookies);
	}
}]);	

function veriricarSeEstaLogado($scope, $http, $window, $cookies) {
	var reqEstaLogado = {
			method: 'POST',
			url: 'http://localhost:8080/restapp/ws/users/estalogado',
			headers: {
				'Content-Type': 'application/json',
				'Authorization': $cookies.get('authorizationToken')
			},
			data: ''
		  }
	    $http(reqEstaLogado).then(
	    		function success(response) {
	    			
	    		},
	    		function error(response) {
	    			if(response.status == 401) {
	    				$window.location.href = '/login/login.html';
	    			}
				}
	    );
}

