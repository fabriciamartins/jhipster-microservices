(function() {
    'use strict';

    angular
        .module('monolithicApp')
        .controller('VendaDetailController', VendaDetailController);

    VendaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Venda'];

    function VendaDetailController($scope, $rootScope, $stateParams, previousState, entity, Venda) {
        var vm = this;

        vm.venda = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('monolithicApp:vendaUpdate', function(event, result) {
            vm.venda = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
