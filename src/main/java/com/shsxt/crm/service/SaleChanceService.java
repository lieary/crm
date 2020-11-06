package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.enums.DevResult;
import com.shsxt.crm.enums.StateStatus;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 营销机会查询操作
     * @param saleChanceQuery
     * @return
     */
    public Map<String,Object> queryByParams(SaleChanceQuery saleChanceQuery){

        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //得到列表
        List<SaleChance> list = saleChanceMapper.querySaleChanceByParams(saleChanceQuery);
        //得到当前的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>();
        if(null!=list&&list.size()>0){
            map.put("code",0);
            map.put("msg","");
            //总记录数
            map.put("count",pageInfo.getTotal());
            //当前页的列表，deta的value不能直接放list集合，list是全部数据，getlist()是当前也数量
            map.put("data",list);
        }
        return map;
    }

    /**
     * 修改营销机会———更新
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED )
    public void updateSaleChance(SaleChance saleChance){
        AssertUtil.isTrue(saleChance.getId() == null,"带更新记录不存在！");
        //查询数据库是否有值
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp == null,"带更新记录不存在！");
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        // 设置默认值    修改时间    设置为当前系统时间
        saleChance.setUpdateDate(new Date());

        // 设置相关字段
        // 判断数据库的指派人是否为空
        if (StringUtils.isBlank(temp.getAssignMan())) {
            // 数据库指派人 为空
            if  (!StringUtils.isBlank(saleChance.getAssignMan())) {
                // 修改后 不为空
                // 设置指派时间 分配状态 开发状态
                saleChance.setAssignTime(new Date()); // 当前系统时间
                saleChance.setState(StateStatus.STATED.getType()); // 已分配
                saleChance.setDevResult(DevResult.DEVING.getStatus()); // 开发中
            }
        } else {
            // 数据库指派人 不为空
            if (StringUtils.isBlank(saleChance.getAssignMan())) {
                // 修改后 为空
                // 设置指派时间 分配状态 开发状态
                saleChance.setAssignTime(null); // 没有指派时间
                saleChance.setState(StateStatus.UNSTATE.getType()); // 未分配
                saleChance.setDevResult(DevResult.UNDEV.getStatus()); // 未开发

            } else {
                // 修改后 不为空
                // 修改前后，指派人是否改变
                if (!temp.getAssignMan().equals(saleChance.getAssignMan())) {
                    // 如果指派人改变，则修改指派时间
                    saleChance.setAssignTime(new Date());
                } else {
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }


        // 执行更新
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1, "营销机会数据修改失败！");

    }

    /**
     * 营销机会添加操作
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        /*做必选项的非空判断*/
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        /*判断是否设置了指派人*/
        //判断指派人是否为空
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //指派人为空,设置分配时间和分配状态,开发状态
            saleChance.setAssignTime(null);
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else {
            //指派人不为空，设置分配时间和分配状态,开发状态
            saleChance.setAssignTime(new Date());
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        /*设置其他形参的默认值*/
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        /*执行添加操作*/
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"营销机会数据添加失败！");

    }

    /**
     * 判断添加参数是否为空的方法
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"客户电话不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"号码格式不正确！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空！");
    }

    /**
     * 删除营销机会
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids) {
        // 非空判断
        AssertUtil.isTrue(null == ids || ids.length < 1, "待删除记录不存在！");
        // 删除
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length, "营销机会数据删除失败！");
    }

    /**
     * 修改开发状态
     * @param saleChanceId
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDevResult(Integer saleChanceId, Integer devResult) {
        // 判断主键ID
        AssertUtil.isTrue(null == saleChanceId, "待更新记录不存在！");
        // 判断状态
        AssertUtil.isTrue(null == devResult, "系统异常，请重试！");
        // 通过id查询对象
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(saleChanceId);
        // 判断对象是否为空
        AssertUtil.isTrue(null == saleChance, "待更新记录不存在！");

        // 修改状态
        saleChance.setDevResult(devResult);
        saleChance.setUpdateDate(new Date());

        // 执行更新
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1, "更新状态失败！");
    }

}
