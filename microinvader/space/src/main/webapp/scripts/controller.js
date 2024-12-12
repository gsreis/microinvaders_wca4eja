
var app = angular.module('space', []);

app.controller('control', [ '$scope', '$http', '$interval', '$window', function($scope, $http, $interval, $window) {
	
	    $interval( function(){ $scope.callAtInterval(); }, 400);
	    
	    $scope.key;
	    $scope.finished = false;
	    $scope.showdialog = true;
	    
	    $scope.space_ip = "http://127.0.0.1:9081/space-1.0";
	    
	    
	    $scope.keyPressed = function($event) {
	    	$scope.callRestKey($event.which);
	    	$scope.userBomb($event);
	    }
    	
	    $scope.userBomb = function($event) {
	    	if ($event.which == 32) { // space create user bombs
	    		for (sprite = 0; sprite < $scope.space.length; sprite++) {
					if ($scope.space[sprite].type == 'player')
						$scope.callRestBomb($scope.space[sprite].x, $scope.space[sprite].y-1, 'true');
				}
	    	}	    	
	    }
	    
		$scope.callAtInterval = function() {
			if ($scope.showdialog){
				 $window.alert("Microinvader game !!!\nKeys for the game\no (letter o) - left\np (letter p) - right\n0 - reset game\nspace - launch bomb\n\nEnjoy !!!");
				 $scope.showdialog = false;
			}
			
			if (!$scope.finished) {
	    		$scope.callRestRun();      // update pieces position
	    		$scope.callRestPosition(); // get position after move
	    		$scope.populateGrid();	   // redraw screen			
			}
    		$scope.callRestFinished(); // check if game ends (Update finished flag)
    	}
    	    	
		$scope.populateGrid = function() {
			if ($scope.space)
			{
	    		for (var y = 0; y < 20; y++) {
	    	        for(var x = 0; x < 20; x++) {
	    	        	sprite = $scope.find(x,y);
	    	        	if (sprite != -1 && !$scope.space[sprite].destroyed)
	    	        	    $scope.grid[y][x] = $scope.space[sprite].ip + "/" +$scope.space[sprite].type + "-1.0/image/"+ $scope.space[sprite].id;
	    	        	else
	    	        		$scope.grid[y][x] = $scope.space_ip + '/image/0';
	    	        }
	    		}				
			}
    	}
    
    	$scope.find = function(x, y)
    	{
    		if ($scope.space) {
     			for (sprite = 0; sprite < $scope.space.length; sprite++) {
					if ($scope.space[sprite].x == x && $scope.space[sprite].y == y && !$scope.space[sprite].destroyed)
						return sprite;
				}   			
    		}
    		return -1;
    	}
    	
    	$scope.grid = [[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19],
	                   [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19]
	                  ];

	   
    	$scope.callRestPosition = function() {
		    $http.get($scope.space_ip + '/rest/position').then(function(response) {
		            $scope.space = response.data;
		        });
    	}
    	
    	$scope.callRestFinished = function() {
		    $http.get($scope.space_ip + '/rest/isFinished').then(function(response) {
		            $scope.finished = response.data.finished;
		        });
    	}
    	
    	$scope.callRestRun = function() {
		    $http.get($scope.space_ip + '/rest/run').then(function(response) {
		        });
    	}
    	$scope.callRestKey = function(side) {
		    $http.get($scope.space_ip + '/rest/move/' + side).then(function(response) {
		        });
    	}  
    	$scope.callRestBomb = function(x, y, player) {
		    $http.get($scope.space_ip + '/rest/create/' + x + '/' + y + '/' + player).then(function(response) {
		        });
    	} 
   }]);
