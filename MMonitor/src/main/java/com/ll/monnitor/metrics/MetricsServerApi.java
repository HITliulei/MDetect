package com.ll.monnitor.metrics;

import com.squareup.okhttp.Call;
import io.kubernetes.client.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/29
 */

/**
 *
 * @author suozq
 *
 */
public class MetricsServerApi {

    private ApiClient localVarApiClient;

    public final static String BASE_URL="/apis/metrics.k8s.io/v1beta1";

    private String url_get_nodes="/nodes";

    private String url_get_namespace_pods = "/namespaces/{namespace}/pods";


    private String url_get_namespace_pod = "/namespaces/{namespace}/pods/{pod}";



    public MetricsServerApi() {
        this(Configuration.getDefaultApiClient());
    }

    public MetricsServerApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    /**
     *   获取集群各个node节点的cpu和内存用量
     */
    public MetricsNodeList getNodesAndUsage() throws ApiException {
        String path = BASE_URL+url_get_nodes;
        Call call = buildGetSimpleCall(path, "GET");
        ApiResponse<MetricsNodeList> r = localVarApiClient.execute(call,MetricsNodeList.class);
        return r.getData();
    }

    /**
     * 获取namespace下pods的cpu和内存用量
     * @param namespace
     * @return
     * @throws ApiException
     */
    public MetricsPodList getPodsAndUsage(String namespace) throws ApiException {
        String path = new String(url_get_namespace_pods);
        path=path.replaceAll("\\{namespace\\}", namespace);
        path = BASE_URL+path;
        Call call = buildGetSimpleCall(path, "GET");
        ApiResponse<MetricsPodList> r = localVarApiClient.execute(call,MetricsPodList.class);
        return r.getData();
    }
    /**
     * 获取namespace下pods的cpu和内存用量
     * @param namespace
     * @param podname
     * @return
     * @throws ApiException
     */
    public MetricsPodList.MetricsPod getPodAndUsage(String podname, String namespace) throws ApiException {
        String path = new String(url_get_namespace_pod);
        path=path.replaceAll("\\{namespace\\}", namespace);
        path = path.replaceAll("\\{pod\\}", podname);
        path = BASE_URL+path;
        Call call = buildGetSimpleCall(path, "GET");
        ApiResponse<MetricsPodList.MetricsPod> r = localVarApiClient.execute(call,MetricsPodList.MetricsPod.class);
        return r.getData();
    }
    /**
     * 构建简单的Call
     */
    private Call buildGetSimpleCall(String path, String method) throws ApiException {
        String[] localVarAuthNames = new String[] { "BearerToken" };
        List<Pair> localVarQueryParams = new ArrayList<>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<>();

        Map<String, String> localVarHeaderParams = new HashMap<>();
        Map<String, Object> localVarFormParams = new HashMap<>();
        final String[] localVarAccepts = {
                "*/*"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
//        return localVarApiClient.buildCall(path, method, localVarQueryParams, localVarCollectionQueryParams, null, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, null);
        return localVarApiClient.buildCall(path,method,localVarQueryParams,localVarCollectionQueryParams,null,localVarHeaderParams,localVarFormParams,localVarAuthNames, null);
    }

}

