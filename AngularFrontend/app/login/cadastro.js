angular.module('myApp', ['ngCookies'])
.controller('cadastroController', ['$scope', '$http', '$window', '$cookies', function($scope, $http, $window, $cookies) {
	$scope.submitCadastro = function() {
		var req = {
				method: 'POST',
				url: 'http://localhost:8080/restapp/ws/users/cadastro',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				data: 'name='+$scope.name+'&login='+$scope.username+'&passwd='+$scope.passwd
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
				}
	    );
		console.log($cookies.get('authorizationToken'));
	}
}]);