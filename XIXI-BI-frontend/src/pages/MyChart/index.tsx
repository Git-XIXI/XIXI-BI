import { listMyChartByPageUsingPOST } from '@/services/xixi/chartController';
import { useModel } from '@@/exports';
import { Card,List,message,Result,Spin } from 'antd';
import Search from 'antd/es/input/Search';
import ReactECharts from 'echarts-for-react';
import React,{ useEffect,useState } from 'react';

/**
 * 我的图表页面
 * @constructor
 */
const MyChartPageAsync: React.FC = () => {
  const initSerarchParms = {
    pageSize: 4,
    current: 1,
    sortField: 'createTime',
    sortOrder: 'desc',
  };
  const [searchParams, setsearchParams] = useState<API.ChartQueryRequest>({
    ...initSerarchParms,
  });
  const [chartList, setCaharList] = useState<API.Chart[]>();
  const [total, setTotal] = useState<number>(0);
  const [genChart, setGenChart] = useState<any>();
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};
  const [loading, setLoading] = useState<boolean>(false);
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listMyChartByPageUsingPOST({ ...searchParams });
      if (res.data) {
        setCaharList(res.data.records ?? []);
        setTotal(res.data.total ?? 0);
        setLoading(false);
      } else {
        message.error('获取图表失败');
      }
    } catch (e: any) {
      message.error('获取图表失败' + e.message);
    }
  };

  useEffect(() => {
    loadData();
  }, [searchParams]);
  return (
    <div className="my-chart-page">
      <div className="margin-16">
        <Search
          placeholder="请输入图表名称"
          enterButton
          loading={loading}
          onSearch={(value) => {
            setsearchParams({
              ...initSerarchParms,
              name: value,
            });
          }}
        />
      </div>
      <List
        grid={{
          gutter: 16,
          xs: 1,
          sm: 1,
          md: 1,
          lg: 1,
          xl: 2,
          xxl: 2,
        }}
        pagination={{
          onChange: (page, pageSize) => {
            setsearchParams({
              ...searchParams,
              current: page,
              pageSize,
            });
            console.log(genChart);
          },
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total: total,
        }}
        dataSource={chartList}
        renderItem={(item) => (
          <List.Item key={item.id}>
            {/*<Result*/}
            {/*  status="error"*/}
            {/*  title="图表生成失败"*/}
            {/*  subTitle={item.execMessage}*/}
            {/*/>*/}
            {/*<Result*/}
            {/*  status="warning"*/}
            {/*  title="待生成"*/}
            {/*  subTitle={item.execMessage ?? '当前图表队列繁忙，请耐心等待'}*/}
            {/*/>*/}
            {/*<div className={'cardDivStyle'}>*/}
            {/*  <Spin tip="图表生成中">*/}
            {/*    <div className={'cardDivStyle'}></div>*/}
            {/*  </Spin>*/}
            {/*</div>*/}
            <Card style={{ width: '100%' }} hoverable={true}>
              {item.status === 'success' && (
                <ReactECharts option={JSON.parse(item.genChart ?? '{}')} />
              )}

              {item.status === 'failed' && (
                <div className={'cardDivStyle'}>
                  <Result status="error" title="图表生成失败" subTitle={item.execMessage} />
                </div>
              )}

              {item.status === 'wait' && (
                <div className={'cardDivStyle'}>
                  <Result
                    status="warning"
                    title="待生成"
                    subTitle={item.execMessage ?? '当前图表队列繁忙，请耐心等待'}
                  />
                </div>
              )}

              {item.status === 'running' && (
                <div className={'cardDivStyle'}>
                  <Spin tip="图表生成中">
                    <div className={'cardDivStyle'}></div>
                  </Spin>
                </div>
              )}

              <div style={{ fontWeight: 'bold', fontSize: '15px', letterSpacing: 1 }}>
                {'分析目标:' + item.goal}
              </div>
            </Card>
          </List.Item>
        )}
      />
    </div>
  );
};

export default MyChartPageAsync;
