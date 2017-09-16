(function() {
    'use strict';

    angular
        .module('monolithicApp')
        .controller('PedidoController', PedidoController);

    PedidoController.$inject = ['Pedido'];

    function PedidoController(Pedido) {

        var vm = this;

        vm.pedidos = [];

        loadAll();

        function loadAll() {
            Pedido.query(function(result) {
                vm.pedidos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
