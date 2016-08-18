var seckill = {
	URL:{
		now :'/seckill/time/now',
		exposer:function(seckillId){
			return '/seckill/'+seckillId+'/exposer'
		},
		execution:function(seckillId,md5){
			return '/seckill/'+seckillId+'/'+md5+'/execution'
		}
	},
	//验证手机号
	validatePhone:function(phone){
		if(phone && phone.length == 11 && !isNaN(phone)){
			return true;
		}else{
			return false;
		}
	},
	//获取秒杀地址，控制显示，执行秒杀
	handleSeckill:function(seckillId,node){
		console.log(seckillId)
		node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
		$.post(seckill.URL.exposer(seckillId),{},function(result){
			if(result && result.success){
				var exposer = result.data;
				if (exposer.exposed) {
					//获取秒杀地址
					var md5 = exposer.md5;
					var killUrl = seckill.URL.execution(seckillId,md5)
					console.log("killUrl:"+killUrl);
					//绑定一次点击事件
					$('#killBtn').one('click',function(){
						//1、先禁用按钮
						$(this).addClass('disabled');
						//2、发送秒杀请求执行秒杀
						$.post(killUrl,{},function(result){
							if(result && result.success){
								var killResult = result.data;
								var state = killResult.state;
								var statInfo = killResult.statInfo;
								//显示秒杀结果
								node.html('<span class="label label-success">'+statInfo+'</span>')
							}
						});
					});
					node.show();
				} else {
					var now = exposer.now;
					var start = exposer.start;
					var end = exposer.end;
					//重新计算计时逻辑
					seckill.countdown(seckillId, now, start, end);
				}
			}else{
				console.log('result:'+result);
			}
		});
	},
	countdown:function(seckillId,nowTime,startTime,endTime){
		var seckillBox = $('#seckill-box');
		//时间判断
		if(nowTime > endTime){
			seckillBox.html('秒杀结束');
		}else if(nowTime < startTime){
			//秒杀未开始，显示倒计时
			var killTime = new Date(startTime + 1000);
			seckillBox.countdown(killTime,function(event){
				var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
				seckillBox.html(format);
			}).on('finish.countdown',function(){
				//时间完成后回庙事件，获取秒杀地址
				seckill.handleSeckill(seckillId,seckillBox);
			});
		}else{
			//秒杀开始
			seckill.handleSeckill(seckillId,seckillBox);
		}
	},
	detail:{
		init:function(params){
			console.log(params)
			var killPhone = $.cookie('killPhone');
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			console.log(seckillId)
			if(!seckill.validatePhone(killPhone)){
				var killPhoneModal = $('#killPhoneModal');
				killPhoneModal.modal({
					//显示弹出层
					show:true,
					backdrop:'static',
					keyboard:false
				});
				$('#killPhoneBtn').click(function(){
					var inputPhone = $('#killPhoneKey').val();
					if(seckill.validatePhone(inputPhone)){
						$.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'})
						window.location.reload();
					}else{
						$('#killPhoneMessage').show(300);
					}
				});
			}
			//计时
			$.get(seckill.URL.now,{},function(result){
				if(result && result['success']){
					var nowTime = result['data'];
					seckill.countdown(seckillId,nowTime,startTime,endTime);
				}else{
					console.log(result)
				}
			});
		}
	}
}