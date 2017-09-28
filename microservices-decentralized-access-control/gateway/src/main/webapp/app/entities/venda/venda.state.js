(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('venda', {
            parent: 'entity',
            url: '/venda',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.venda.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/venda/vendas.html',
                    controller: 'VendaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('venda');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('venda-detail', {
            parent: 'venda',
            url: '/venda/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.venda.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/venda/venda-detail.html',
                    controller: 'VendaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('venda');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Venda', function($stateParams, Venda) {
                    return Venda.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'venda',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('venda-detail.edit', {
            parent: 'venda-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/venda/venda-dialog.html',
                    controller: 'VendaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Venda', function(Venda) {
                            return Venda.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('venda.new', {
            parent: 'venda',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/venda/venda-dialog.html',
                    controller: 'VendaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idPedido: null,
                                valor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('venda', null, { reload: 'venda' });
                }, function() {
                    $state.go('venda');
                });
            }]
        })
        .state('venda.edit', {
            parent: 'venda',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/venda/venda-dialog.html',
                    controller: 'VendaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Venda', function(Venda) {
                            return Venda.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('venda', null, { reload: 'venda' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('venda.delete', {
            parent: 'venda',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/venda/venda-delete-dialog.html',
                    controller: 'VendaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Venda', function(Venda) {
                            return Venda.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('venda', null, { reload: 'venda' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
