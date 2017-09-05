(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pedido', {
            parent: 'entity',
            url: '/pedido',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.pedido.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pedido/pedidos.html',
                    controller: 'PedidoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pedido');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pedido-detail', {
            parent: 'pedido',
            url: '/pedido/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.pedido.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pedido/pedido-detail.html',
                    controller: 'PedidoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pedido');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pedido', function($stateParams, Pedido) {
                    return Pedido.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pedido',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pedido-detail.edit', {
            parent: 'pedido-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedido/pedido-dialog.html',
                    controller: 'PedidoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pedido', function(Pedido) {
                            return Pedido.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pedido.new', {
            parent: 'pedido',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedido/pedido-dialog.html',
                    controller: 'PedidoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idUsuario: null,
                                idProduto: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pedido', null, { reload: 'pedido' });
                }, function() {
                    $state.go('pedido');
                });
            }]
        })
        .state('pedido.edit', {
            parent: 'pedido',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedido/pedido-dialog.html',
                    controller: 'PedidoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pedido', function(Pedido) {
                            return Pedido.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pedido', null, { reload: 'pedido' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pedido.delete', {
            parent: 'pedido',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedido/pedido-delete-dialog.html',
                    controller: 'PedidoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pedido', function(Pedido) {
                            return Pedido.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pedido', null, { reload: 'pedido' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
