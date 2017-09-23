(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('VendaController', VendaController);

    VendaController.$inject = ['Venda'];

    function VendaController(Venda) {

        var vm = this;

        vm.vendas = [];

        loadAll();

        function loadAll() {
            Venda.query(function(result) {
                vm.vendas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
