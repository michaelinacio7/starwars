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
	    			$scope.listaPessoas = [];
	    			$scope.count = 0;
	    			$scope.starships = [];
	    			$scope.countStarShips = 0;
	    			findPersons('http://swapi.co/api/people/?format=json', $scope, $http);
	    			findStarShips('http://swapi.co/api/starships/?format=json', $scope, $http);
	    			var id = $cookies.get('idEspaconaveSelecionada');
	    			$cookies.remove('idEspaconaveSelecionada', {path: '/'});
	    			if(id != null) {
	    				$http({
	    					url: "http://localhost:8080/restapp/ws/rest/editarFrota",
	    					method: "POST",
	    					headers: {'Content-Type': 'application/json', 
	    					   'Authorization': $cookies.get('authorizationToken')},
	    					data: id
	    				  }).then(
	    						function success(response) {
	    							findStarShipById('http://swapi.co/api/starships/'+response.data.idEspaconave+'?format=json', $scope, $http);
	    							$scope.idSelecionada = response.data.id;
	    							if(response.data.tripulantes[0] != null) {
	    								setarTripulante1('http://swapi.co/api/people/'+response.data.tripulantes[0].id, $scope, $http);
	    							}
	    							if(response.data.tripulantes[1] != null) {
	    								setarTripulante2('http://swapi.co/api/people/'+response.data.tripulantes[1].id, $scope, $http);
	    							}
	    							if(response.data.tripulantes[2] != null) {
	    								setarTripulante3('http://swapi.co/api/people/'+response.data.tripulantes[2].id, $scope, $http);
	    							}
	    							if(response.data.tripulantes[3] != null) {
	    								setarTripulante4('http://swapi.co/api/people/'+response.data.tripulantes[3].id, $scope, $http);
	    							}
	    							
	    						},
	    						function error(response) {
	    							if(response.status == 401) {
	    								$window.location.href = '/login/login.html';
	    						}
	    				});
	    			}
	    		},
	    		function error(response) {
	    			if(response.status == 401) {
	    				$window.location.href = '/login/login.html';
	    			}
				}
	    );
	
    $scope.submit = function() {
       var contador = 0;
       var arrayTripulantes = [];
       
       if($scope.tripulante1 != null)
       { 
    	 if($scope.tripulante1.description != null) {
    		 arrayTripulantes[contador] = { id: $scope.tripulante1.description.id, name: $scope.tripulante1.description.name };
    	 } else {
    		 arrayTripulantes[contador] = { id: $scope.tripulante1.originalObject.id, name: $scope.tripulante1.originalObject.name };
    	 }
         contador++;
       }    
    
       if($scope.tripulante2 != null)
       { 
    	   if($scope.tripulante2.description != null) {
    		   arrayTripulantes[contador] = { id: $scope.tripulante2.description.id, name: $scope.tripulante2.description.name };
    	   } else {
    		   arrayTripulantes[contador] = { id: $scope.tripulante2.originalObject.id, name: $scope.tripulante2.originalObject.name };
    	   }
        contador++;
       }

       if($scope.tripulante3 != null)
       {
    	   if($scope.tripulante3.description != null) {
    		   arrayTripulantes[contador] = { id: $scope.tripulante3.description.id, name: $scope.tripulante3.description.name };
    	   } else {
    		   arrayTripulantes[contador] = { id: $scope.tripulante3.originalObject.id, name: $scope.tripulante3.originalObject.name };
    	   }
         contador++; 
       }

       if($scope.tripulante4 != null)
       {
    	   if($scope.tripulante4.description != null) {
    		   arrayTripulantes[contador] = { id: $scope.tripulante4.description.id, name: $scope.tripulante4.description.name };
    	   } else {
    		   arrayTripulantes[contador] = { id: $scope.tripulante4.originalObject.id, name: $scope.tripulante4.originalObject.name };
    	   }
         contador++; 
       }

       if(contador < 1)
       {
         alert("Selecione pelo menos um Tripulante!");
         return;
       }

       for(var i=0; i < arrayTripulantes.length; i++)
       {
    	   for(var j=0; j < arrayTripulantes.length; j++)
           {
    		   if (i != j)
               {
            	  if(arrayTripulantes[i]['id'] == arrayTripulantes[j]['id'])
            	  {
            		  alert("Não é possivel selecionar o mesmo tripulante mais de uma vez!");  
            		  return;
            	  }
               }
           }
       }
       

       var jsonObj = {
    	 id: $scope.idSelecionada,
         idEspaconave: $scope.selectedStarShip.description != null ? $scope.selectedStarShip.description.id : $scope.selectedStarShip.originalObject.id,
         nome:  $scope.selectedStarShip.description != null ? $scope.selectedStarShip.description.name : $scope.selectedStarShip.originalObject.name,
         idUser: $cookies.get('id_user'), 
         tripulantes : arrayTripulantes          
       };

       $http({
               url: "http://localhost:8080/restapp/ws/rest/cadastrarFrota",
               method: "POST",
               headers: {'Content-Type': 'application/json', 
            	   'Authorization': $cookies.get('authorizationToken')},
               data: jsonObj
             }).then(
     	    		function success(response) {
    	    			alert("Gravação realizada com sucesso!!");
    	    			$window.location.href = 'starship.html';
    	    		},
    	    		function error(response) {
    	    			if(response.status == 401) {
    	    				alert("Não foi possível gravar os registros!");
    	    				$window.location.href = '/login/login.html';
    	    			}
    				});
    }
    
    
}]);

function findPersons($url, $scope, $http) {
  $http.get($url).
        then(function(response) {
            $scope.data = response.data;
            angular.forEach($scope.data.results, function(value, key) {
              $scope.listaPessoas[$scope.count] = value;
              $scope.listaPessoas[$scope.count]['id'] = $scope.listaPessoas[$scope.count].url.replace('http://swapi.co/api/people/', '').replace('/', '');
              $scope.count++;
            });
           if($scope.data.next != null) {
              findPersons($scope.data.next, $scope, $http);
           }
        });
}

function findStarShips($url, $scope, $http) {
  $http.get($url).
        then(function(response) {
            $scope.dataStarship = response.data;
            angular.forEach($scope.dataStarship.results, function(value, key) {
              $scope.starships[$scope.countStarShips] = value;
              $scope.starships[$scope.countStarShips]['id'] = $scope.starships[$scope.countStarShips].url.replace('http://swapi.co/api/starships/', '').replace('/', '');
              $scope.countStarShips++;
            });
           if($scope.dataStarship.next != null) {
              findStarShips($scope.dataStarship.next, $scope, $http);
           }
        });
}

function findStarShipById($url, $scope, $http) {
	  $http.get($url)
	  		.then(function(response) {
	            $scope.selectedStarShip1 = response.data;
	            $scope.selectedStarShip1.description = response.data;
	            $scope.selectedStarShip1['id'] = response.data.url.replace('http://swapi.co/api/starships/', '').replace('/', '');
	        },
	        function(response) {
	        });
	}

function setarTripulante1($url, $scope, $http) {
	  $http.get($url).
	        then(function(response) {
	        	$scope.tripulante1Init = response.data;
	        	$scope.tripulante1Init['description'] = response.data;
	        	$scope.tripulante1Init['id'] = response.data.url.replace('http://swapi.co/api/people/', '').replace('/', '');
	        });
}
function setarTripulante2($url, $scope, $http) {
	$http.get($url).
	then(function(response) {
		$scope.tripulante2Init = response.data;
		$scope.tripulante2Init['description'] = response.data;
		$scope.tripulante2Init['id'] = response.data.url.replace('http://swapi.co/api/people/', '').replace('/', '');
	});
}
function setarTripulante3($url, $scope, $http) {
	$http.get($url).
	then(function(response) {
		$scope.tripulante3Init = response.data;
		$scope.tripulante3Init['description'] = response.data;
		$scope.tripulante3Init['id'] = response.data.url.replace('http://swapi.co/api/people/', '').replace('/', '');
	});
}
function setarTripulante4($url, $scope, $http) {
	$http.get($url).
	then(function(response) {
		$scope.tripulante4Init = response.data;
		$scope.tripulante4Init['description'] = response.data;
		$scope.tripulante4Init['id'] = response.data.url.replace('http://swapi.co/api/people/', '').replace('/', '');
	});
}


