angular.module('myApp', ['ngCookies'])
.controller('loginController', ['$scope', '$http', '$window', '$cookies', function($scope, $http, $window, $cookies) {
	$scope.submitLogin = function() {
		var req = {
				method: 'POST',
				url: 'http://localhost:8080/restapp/ws/users/login',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				data: 'login='+$scope.username+'&passwd='+$scope.passwd
		}

		var requisicao = {
				method: 'POST',
				url: 'http://localhost:8080/restapp/ws/users/findUser',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				data: 'login='+$scope.username
		}
		
		$http(req).then(
	    		function success(response) {
	    			$cookies.put('authorizationToken', response.headers('authorization'), {path: '/'});
	    			$cookies.put('name', $scope.username, {path: '/'});
	    			$http(requisicao).then(
	    					function success(response) {
	    						$cookies.put('id_user', response.data, {path: '/'});
	    					}
	    			);
	    			$window.location.href = '/';
	    		},
	    		function error(response) {
	    			console.log(response);
	    			alert("Usuário ou Senha Invalido!");
				}
	    );
	}
}]);