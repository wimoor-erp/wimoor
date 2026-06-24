import request from "@/utils/request.js";

function getMonthDetail(data) {
  return request.post("/amazon/api/v1/settlement/getMonthDetail",  data  );
}

function getMonthReport(data) {
  return request.post("/amazon/api/v1/settlement/getMonthReport",  data  );
}

export default {
  getMonthDetail,
  getMonthReport
};