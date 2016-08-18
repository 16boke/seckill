<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>秒杀详情页</title>
<%@include file="common/head.jsp"%>
</head>
<body>
	<div class="modal fade" id="killPhoneModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" data-backdrop="static"
		data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h3 class="modal-title">
						<span class="glphyicon glphyicon-phone"></span>秒杀电话:
					</h3>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-8 clo-xs-offset-2">
							<input type="text" name="killPhone" id="killPhoneKey"
								placeholder="填写手机号^o^" class="form-control">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<div class="alert alert-warning"
						style="display: none; margin-bottom: 0; padding-top: 0; padding-bottom: 0;"
						role="alert" id="killPhoneMessage">
						<strong>Warning!</strong> 手机号错误!
					</div>
					<button type="button" id="killPhoneBtn" class="btn btn-success">
						<span class="glyphicon glyphicon-phone"></span> submit
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="panel panel-default text-center">
			<div class="panel-heading">
				<h1>${seckill.name}</h1>
			</div>
			<div class="panel-body">
				<h2 class="text-danger">
					<span class="glyphicon glyphicon-time"></span> <span
						class="glyphicon" id="seckill-box"></span>
				</h2>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="http://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
	<script type="text/javascript" src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
	<script type="text/javascript" src="/js/seckill.js"></script>
	<script type="text/javascript">
      $(function () {
        seckill.detail.init({
          seckillId : ${seckill.id},
          startTime : ${seckill.startTime.time},
          endTime : ${seckill.endTime.time}
        });
      });
    </script>
</body>
</html>