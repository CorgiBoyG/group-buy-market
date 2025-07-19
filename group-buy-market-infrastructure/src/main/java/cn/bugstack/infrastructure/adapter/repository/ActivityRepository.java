package cn.bugstack.infrastructure.adapter.repository;


import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.valobj.DiscountTypeEnum;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.model.valobj.SCSkuActivityVO;
import cn.bugstack.domain.activity.model.valobj.SkuVO;
import cn.bugstack.infrastructure.dao.IGroupBuyActivityDao;
import cn.bugstack.infrastructure.dao.IGroupBuyDiscountDao;
import cn.bugstack.infrastructure.dao.ISCSkuActivityDao;
import cn.bugstack.infrastructure.dao.ISkuDao;
import cn.bugstack.infrastructure.dao.po.GroupBuyActivity;
import cn.bugstack.infrastructure.dao.po.GroupBuyDiscount;
import cn.bugstack.infrastructure.dao.po.SCSkuActivity;
import cn.bugstack.infrastructure.dao.po.Sku;
import cn.bugstack.infrastructure.dcc.DCCService;
import cn.bugstack.infrastructure.redis.IRedisService;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.infrastructure.adapter.repository
 * @Description: 活动仓储
 * @Author: Daniel G
 * @Create: 2025-07-16 10:15:12
 */

@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;

    @Resource
    private IGroupBuyDiscountDao groupBuyDiscountDao;

    @Resource
    private ISkuDao skuDao;

    @Resource
    private ISCSkuActivityDao skuActivityDao;

    @Resource
    private IRedisService redisService;

    @Resource
    private DCCService dccService;

    @Override
    public GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId) {

        // 根据活动id 查询拼团活动配置中的1个有效的活动
        GroupBuyActivity groupBuyActivityRes = groupBuyActivityDao.queryValidGroupBuyActivityId(activityId);
        if (null == groupBuyActivityRes) return null;

        //获取优惠配置Id （Id唯一）
        String discountId = groupBuyActivityRes.getDiscountId();

        //获取该活动对应的优惠配置
        GroupBuyDiscount groupBuyDiscountRes =
                groupBuyDiscountDao.queryGroupBuyActivityDiscountByDiscountId(discountId);
        if (null == groupBuyDiscountRes) return null;

        //封装VO
        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount =
                GroupBuyActivityDiscountVO.GroupBuyDiscount.builder()
                        .discountName(groupBuyDiscountRes.getDiscountName())
                        .discountDesc(groupBuyDiscountRes.getDiscountDesc())
                        .discountType(DiscountTypeEnum.get(groupBuyDiscountRes.getDiscountType()))
                        .marketPlan(groupBuyDiscountRes.getMarketPlan())
                        .marketExpr(groupBuyDiscountRes.getMarketExpr())
                        .tagId(groupBuyDiscountRes.getTagId())
                        .build();

        return GroupBuyActivityDiscountVO.builder()
                .activityId(groupBuyActivityRes.getActivityId())
                .activityName(groupBuyActivityRes.getActivityName())
                .groupBuyDiscount(groupBuyDiscount)
                .groupType(groupBuyActivityRes.getGroupType())
                .takeLimitCount(groupBuyActivityRes.getTakeLimitCount())
                .target(groupBuyActivityRes.getTarget())
                .validTime(groupBuyActivityRes.getValidTime())
                .status(groupBuyActivityRes.getStatus())
                .startTime(groupBuyActivityRes.getStartTime())
                .endTime(groupBuyActivityRes.getEndTime())
                .tagId(groupBuyActivityRes.getTagId())
                .tagScope(groupBuyActivityRes.getTagScope())
                .build();
    }

    @Override
    public SkuVO querySkuByGoodsId(String goodsId) {
        //根据商品id查询商品信息
        Sku sku = skuDao.querySkuByGoodsId(goodsId);
        if (null == sku) return null;

        //封装Vo
        return SkuVO.builder()
                .goodsId(sku.getGoodsId())
                .goodsName(sku.getGoodsName())
                .originalPrice(sku.getOriginalPrice())
                .build();
    }

    @Override
    public SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId) {
        SCSkuActivity scSkuActivityReq = new SCSkuActivity();
        scSkuActivityReq.setSource(source);
        scSkuActivityReq.setChannel(channel);
        scSkuActivityReq.setGoodsId(goodsId);

        //实际上Dao中就用了goodsId进行查询
        SCSkuActivity scSkuActivity = skuActivityDao.querySCSkuActivityBySCGoodsId(scSkuActivityReq);
        if (null == scSkuActivity) return null;

        return SCSkuActivityVO.builder()
                .source(scSkuActivity.getSource())
                .channel(scSkuActivity.getChannel())
                .activityId(scSkuActivity.getActivityId())
                .goodsId(scSkuActivity.getGoodsId())
                .build();
    }

    @Override
    public boolean isTagCrowdRange(String tagId, String userId) {
        if (null == tagId) return true;//不存在说明没有限制
        RBitSet bitSet = redisService.getBitSet(tagId);
        if (!bitSet.isExists()) return true;//不存在说明没有限制
        // 判断用户是否存在人群中
        return bitSet.get(redisService.getIndexFromUserId(userId));
    }

    @Override
    public boolean downgradeSwitch() {
        return dccService.isDowngradeSwitch();
    }

    @Override
    public boolean cutRange(String userId) {
        return dccService.isCutRange(userId);
    }
}
