package com.gots.intelligentnursing.business;


import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.gots.intelligentnursing.entity.LocationData;

/**
 * @author zhqy
 * @date 2018/5/29
 */

public class RoutePlanningHelper {

    private static final double MAX_WALK_DISTANCE_M = 4000;

    private RoutePlanSearch mSearch;

    public RoutePlanningHelper(OnPlanningResultListener onPlanningResultListener) {
        mSearch = RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener routePlanningListener = new RoutePlanningListener(onPlanningResultListener);
        mSearch.setOnGetRoutePlanResultListener(routePlanningListener);
    }

    public void planning(LocationData startPoint, LocationData endPoint) {
        LatLng startPointLatLng = new LatLng(startPoint.getLatitude(), startPoint.getLongitude());
        LatLng endPointLatLng = new LatLng(endPoint.getLatitude(), endPoint.getLongitude());
        double distance = DistanceUtil.getDistance(startPointLatLng, endPointLatLng);
        if (distance <= MAX_WALK_DISTANCE_M) {
            planningForWalk(startPointLatLng, endPointLatLng);
        } else {
            planningForDrive(startPointLatLng, endPointLatLng);
        }
    }

    private void planningForWalk(LatLng starting, LatLng ending) {
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(PlanNode.withLocation(starting))
                .to(PlanNode.withLocation(ending)));
    }

    private void planningForDrive(LatLng starting, LatLng ending) {
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(PlanNode.withLocation(starting))
                .to(PlanNode.withLocation(ending)));
    }

    public void destroy() {
        mSearch.destroy();
    }

    private static class RoutePlanningListener implements OnGetRoutePlanResultListener {

        private OnPlanningResultListener mListener;

        private RoutePlanningListener(OnPlanningResultListener listener) {
            mListener = listener;
        }

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            // 步行路线结果回调
            mListener.onGetWalkingRouteResult(walkingRouteResult);
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            // 驾车路线结果回调
            mListener.onGetDrivingRouteResult(drivingRouteResult);
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            // 换乘路线结果回调
        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
            // 跨城公共交通路线结果回调
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
            // 室内路线规划回调
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            // 骑行路线结果回调
        }
    }

    public interface OnPlanningResultListener {
        /**
         * 获取到驾车路线规划回调
         * @param drivingRouteResult 驾车路线结果
         */
        void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult);

        /**
         * 获取到步行路线规划回调
         * @param walkingRouteResult 步行路线结果
         */
        void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult);
    }

}
