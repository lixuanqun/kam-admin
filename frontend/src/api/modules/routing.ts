import request from '../interceptor';

// ===== 动态路由 =====
export const getDrGateways = (params: any) => request.get('/drouting/gateways', { params });
export const createDrGateway = (data: any) => request.post('/drouting/gateways', data);
export const updateDrGateway = (id: number, data: any) => request.patch(`/drouting/gateways/${id}`, data);
export const deleteDrGateway = (id: number) => request.delete(`/drouting/gateways/${id}`);
export const getDrRules = (params: any) => request.get('/drouting/rules', { params });
export const createDrRule = (data: any) => request.post('/drouting/rules', data);
export const updateDrRule = (id: number, data: any) => request.patch(`/drouting/rules/${id}`, data);
export const deleteDrRule = (id: number) => request.delete(`/drouting/rules/${id}`);
export const getDrGroups = (params: any) => request.get('/drouting/groups', { params });
export const getDrCarriers = (params: any) => request.get('/drouting/carriers', { params });
export const reloadDrouting = () => request.post('/drouting/reload');
export const getDrStats = () => request.get('/drouting/stats');

// ===== LCR 路由 =====
export const getLcrGateways = (params: any) => request.get('/lcr/gateways', { params });
export const getLcrRules = (params: any) => request.get('/lcr/rules', { params });
export const getLcrTargets = (params: any) => request.get('/lcr/targets', { params });
export const reloadLcr = () => request.post('/lcr/reload');

// ===== 运营商路由 =====
export const getCarrierNames = () => request.get('/carrierroute/carriers');
export const createCarrierName = (carrier: string) => request.post('/carrierroute/carriers', { carrier });
export const deleteCarrierName = (id: number) => request.delete(`/carrierroute/carriers/${id}`);
export const getCarrierDomains = () => request.get('/carrierroute/domains');
export const createCarrierDomain = (domain: string) => request.post('/carrierroute/domains', { domain });
export const deleteCarrierDomain = (id: number) => request.delete(`/carrierroute/domains/${id}`);
export const getCarrierFailureRoutes = (params: any) => request.get('/carrierroute/failure-routes', { params });
export const reloadCarrierroute = () => request.post('/carrierroute/reload');
export const dumpCarrierRoutes = () => request.get('/carrierroute/dump');

// ===== 拨号计划 =====
export const getDialplanRules = (params: any) => request.get('/dialplan', { params });
export const createDialplanRule = (data: any) => request.post('/dialplan', data);
export const updateDialplanRule = (id: number, data: any) => request.patch(`/dialplan/${id}`, data);
export const deleteDialplanRule = (id: number) => request.delete(`/dialplan/${id}`);
export const reloadDialplan = () => request.post('/dialplan/reload');
export const translateDialplan = (dpid: number, input: string) => request.get('/dialplan/translate', { params: { dpid, input } });

// ===== Mtree =====
export const getMtreeRecords = (params: any) => request.get('/mtree', { params });
export const createMtreeRecord = (data: any) => request.post('/mtree', data);
export const deleteMtreeRecord = (id: number) => request.delete(`/mtree/${id}`);
export const reloadMtree = (tname: string) => request.post('/mtree/reload', { tname });

// ===== PDT =====
export const getPdtRecords = (params: any) => request.get('/pdt', { params });
export const createPdtRecord = (data: any) => request.post('/pdt', data);
export const deletePdtRecord = (id: number) => request.delete(`/pdt/${id}`);
export const reloadPdt = () => request.post('/pdt/reload');
