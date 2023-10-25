import {genChartByAiAsyncMqUsingPOST, genChartByAiAsyncUsingPOST} from '@/services/xixi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Card, Form, message, Select, Space, Upload } from 'antd';
import { useForm } from 'antd/es/form/Form';
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';

/**
 * 添加图表（异步）页面
 * @constructor
 */
const AddChart: React.FC = () => {
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [form] = useForm();
  const onFinish = async (values: any) => {
    // 避免重复提交
    if (submitting) {
      return;
    }
    const parmas = {
      ...values,
      file: undefined,
    };
    try {
      //const res = await genChartByAiAsyncUsingPOST(parmas, {}, values.file.file.originFileObj);
      const res = await genChartByAiAsyncMqUsingPOST(parmas, {}, values.file.file.originFileObj);
      if (!res?.data) {
        message.success('分析失败');
      } else {
        message.success('分析任务提交成功，请稍后在我的图表查看结果');
        form.resetFields();
      }
    } catch (e: any) {
      message.error('分析失败' + e.message);
    }

    setSubmitting(false);
  };
  return (
    <div className="add-chart">
      <Card title={'智能分析'}>
        <Form
          name="addChart"
          onFinish={onFinish}
          initialValues={{}}
          labelAlign={'right'}
          labelCol={{ span: 3 }}
        >
          <Form.Item
            name="goal"
            label="分析目标"
            rules={[{ required: true, message: '请输入分析目标!' }]}
          >
            <TextArea placeholder="请输入你的分析需求，比如：分析网站用户的增长情况" />
          </Form.Item>

          <Form.Item name="name" label="图表名称">
            <input placeholder="请输入图表名称" />
          </Form.Item>
          <Form.Item label="图表类型" name="chartType">
            <Select
              style={{ width: 169 }}
              options={[
                { value: '折线图', label: '折线图' },
                { value: '柱状图', label: '柱状图' },
                { value: '堆叠图', label: '堆叠图' },
                { value: '饼图', label: '饼图' },
                { value: '雷达图', label: '雷达图' },
              ]}
            ></Select>
          </Form.Item>

          <Form.Item name="file" label="原始数据">
            <Upload name="file" maxCount={1}>
              <Button icon={<UploadOutlined />}>上传Excel文件</Button>
            </Upload>
          </Form.Item>

          <Form.Item wrapperCol={{ span: 12, offset: 6 }}>
            <Space>
              <Button type="primary" htmlType="submit" loading={submitting} disabled={submitting}>
                提交
              </Button>
              <Button htmlType="reset">重置</Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default AddChart;
