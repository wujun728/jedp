export const examineModel = {
  examineType: 1,
  name: '审批人',
  conditionList: [],
  deptList: [],
  examineErrorHandling: 2, // 1 自动通过 2 管理员审批
  parentLevel: 1,
  tempParentLevel: 1, // 临时变量
  roleId: '',
  type: 1,
  userList: [],
  chooseType: 1, // 1 自选一人 2 自选多人
  overType: 1, // 同时不超过发起人向上的 1 0 临时变量
  rangeType: 1, // 1 全公司 2 指定成员 3 指定角色
  isError: false
}

export const conditionListModel = {
  examineType: 0, // 0 条件 1 指定成员 2 主管 3 角色 4 发起人自选 5 连续多级主管
  name: '',
  conditionList: [
    {
      conditionName: '条件',
      conditionDataList: [],
      examineDataList: [],
      isError: false
    },
    {
      conditionName: '条件',
      conditionDataList: [],
      examineDataList: [],
      isError: false
    }
  ]
}

export const conditionModel = {
  conditionName: '条件',
  conditionDataList: [],
  examineDataList: [],
  isError: false
}
