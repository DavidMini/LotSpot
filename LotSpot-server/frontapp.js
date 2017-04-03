
var myApp = angular.module('myApp', []);

myApp.controller("myCont" +
    "roller", function($scope,$http){

    $scope.newLot = {};
    $scope.clickedLot = {};
    $scope.alertMassege = "";

    $scope.lots = [

    ];

    $scope.saveLot = function(){
        console.log('saving');
        $http({
            method:'POST',
            url:'/api/lots',
            data: {
                name: $scope.newLot.name,
                address: $scope.newLot.address,
                lat: $scope.newLot.lat,
                lng: $scope.newLot.lng,
                phoneNumber: $scope.newLot.phoneNumber,
                capacity: $scope.newLot.capacity,
                occupancy: $scope.newLot.occupancy,
                price: $scope.newLot.price,
                paymentType: $scope.newLot.paymentType,
                hours: $scope.newLot.hours,
                handicapParking: $scope.newLot.handicapParking
            }
        }).then(function(res){

            $scope.lots.push($scope.newLot);
            $scope.newLot = {};

            $scope.alertMassege = "New item add on list successfully!!";
        });

    };


    $scope.selectLot = function(lot){

        $scope.clickedLot = lot;
    };

    $scope.updateLot = function (){
        $http({
            method:'PUT',
            url:'/api/lots/:id',
            params: {id: $scope.clickedLot._id},
            data: {
                name: $scope.newLot.name,
                address: $scope.newLot.address,
                lat: $scope.newLot.lat,
                lng: $scope.newLot.lng,
                phoneNumber: $scope.newLot.phoneNumber,
                capacity: $scope.newLot.capacity,
                occupancy: $scope.newLot.occupancy,
                price: $scope.newLot.price,
                paymentType: $scope.newLot.paymentType,
                hours: $scope.newLot.hours,
                handicapParking: $scope.newLot.handicapParking
            }
        }).then(function(res){

            $scope.alertMassege = "Update Successfully!!";
        });

    };


    $scope.deleteLot = function(){

        console.log('delete lot');
        $http({
            method:'DELETE',
            url:'/api/lots',
            params: {id: $scope.clickedLot._id}
        }).then(function(res){

            $scope.lots.splice($scope.lots.indexOf($scope.clickedLot), 1);
            $scope.alertMassege = "Deleted successfully!!";
        });

    };

    $scope.clickedAlert = function(){
        $scope.alertMassege = "";
    };

    $scope.getLots     = function () {
        console.log('getting lots');
        $http({
            method:'GET',
            url:'/api/lots'

        }).then(function(res){

            res.data.forEach(function(lot){
                $scope.lots.push(lot);
            });
        });
    }

    $scope.getLots();

});