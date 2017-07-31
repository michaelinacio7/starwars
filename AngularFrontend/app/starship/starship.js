angular.module('myApp', ['angucomplete-alt', 'ngCookies'])
.controller('starShipController', ['$scope', '$http', '$window', '$cookies', function($scope, $http, $window, $cookies) {
	$scope.listaEspaconave = [];
	$scope.allNoticias = {};
	$scope.nomeUsuario = '';
	$scope.listaPessoas = [];
	$scope.idEspaconave = 0;
	$scope.nomeEspaconave = '';
	
	$scope.nomeUsuario = $cookies.get('name');
	
	$scope.sair = function() {
		$cookies.put('authorizationToken', '', {path: '/'});
		$window.location.href = '/login/login.html';
	}
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
	    			$scope.lista = [];
	    		    $scope.count = 0;
	    		    $scope.starships = [];
	    		    $scope.countStarShips = 0;
	    		    listaEspaconaves($http, $scope, $cookies);
	    		},
	    		function error(response) {
	    			if(response.status == 401) {
	    				$window.location.href = '/login/login.html';
	    			}
				}
	    );
 
    
    $scope.novaFrota = function() {
    	$window.location.href = 'cadastro.html';
    }
    
    $scope.editarFrota = function(espacoNave) {
    	$cookies.put('idEspaconaveSelecionada', espacoNave.id, {path: '/'});
    	$window.location.href = 'cadastro.html';
   	}
    
}]);

function listaEspaconaves($http, $scope, $cookies) {
	$scope.nomeUsuario = $cookies.get('name');
	
	$http({
        url: "http://localhost:8080/restapp/ws/rest/listarEspaconaves",
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        data: $cookies.get('id_user')
      }).then(
	    		function success(response) {
	    			$scope.listaEspaconave = response.data;
	    		},
	    		function error(response) {
				
	    		}
	    );
}

