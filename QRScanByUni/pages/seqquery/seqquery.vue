<template>
	<view class="container">
		<view class="uni-padding-wrap uni-common-mt">
			<input class="uni-input" v-model="prdno" focus placeholder="制单" style="border: 1px solid #e6e6e7; border-radius: 5px;" />
			<view class="uni-flex uni-row" style="margin-top: 1%;">
				<input class="uni-input" v-model="bedno" focus placeholder="床号" style="flex:1; border: 1px solid #e6e6e7; border-radius: 5px;" />
				<input class="uni-input" v-model="seq" focus placeholder="扎号" style="flex:1;border: 1px solid #e6e6e7; border-radius: 5px;" />
			</view>
			<view class="uni-flex uni-row" style="margin-top: 1%;">
				<view style="flex: 1;">
					<button type="primary" @click="scan" class="imgbox">
						<!-- app -->
						<img src="static/sao.png" alt="" class="img">
						
						<!-- 小程序 -->
						<!-- <img src="../../static/sao.png" alt="" class="img"> -->
						<span style="margin-left: 20%;color: white;">扫一扫</span></button>
				</view>
			</view>
		</view>
		<view style="margin-top: 1%;">
			<table style="font-size: 12px;" v-show="showSeq">
				<thead>
					<tr style=" text-align: center;">
						<th>工序</th>
						<th>工序名称</th>
						<th>数量</th>
						<th>日期</th>
						<th>姓名</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="(item, index) in tableSeqData" style="text-align: center;">
						<td style='width: 75px;'>{{item.prdmoedl.item}}</td>
						<td style='width: 130px;'>{{item.prdmoedl.descn}}</td>
						<td style='width: 20px;'>{{item.qty}}</td>
						<td style='width: 40px;'>{{item.recdate}}</td>
						<td style='width: 50px;'>{{item.emp.name}}</td>
						<td style='width: 60px;'>
							<button type="primary" @click="updateNum(index)" style="font-size: 5rpx;padding: 0px;">修改</button>
						</td>
					</tr>
				</tbody>
			</table>
		</view>
		<uni-popup id="dialogInput" ref="dialogInput" type="dialog">
			<uni-popup-dialog mode="input" title="请输入数量" value="" placeholder="数量" @close="dialogClose" @confirm="dialogInputConfirm"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue'
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue'
	export default {
		components: {
			uniPopup,
			uniPopupMessage,
			uniPopupDialog
		},
		data() {
			return {
				prdno: '',
				bedno: '',
				seq: '',
				facno: '',
				address: '',
				workno: '',
				name: '',
				showSeq: false,
				tableSeqData: []
			}
		},
		methods: {
			dialogInputConfirm(done, val) {
				var scandata={
					qty:val,
					id:this.qrParam.id
				}
				console.log(scandata)
				var _this = this;
				uni.request({
					url: 'http://' + _this.address + '/updateQty',
					data: scandata,
					method: "POST",
					success: (res) => {
						console.log(1)
						console.log(res);
					},
					fail: (error) => {
						uni.showModal({
							showCancel: false,
							content: "更新失败"
						})
					}
				});
				done();
			},
			dialogClose(done) {
				this.msgType = 'info'
				this.message = '点击了对话框的取消按钮'
				this.$refs.popupMessage.open()
				// 需要执行 done 才能关闭对话框
			},
			updateNum(e) {
				var row = this.tableSeqData[e];
				this.qrParam = row
				this.$refs.dialogInput.open();
				console.log(row);
			},
			scan() {
				var _this = this;
				uni.scanCode({
					success: function(res) {
						console.log(res.charSet);
						// console.log(_this._uniencode(res.result));
						var str = res.result.split("@");
						_this.prdno = str[0];
						_this.facno = str[1];
						_this.bedno = str[2];
						_this.seq = str[3];
						// console.log(param);
						
						uni.request({
							url: 'http://' + _this.address + '/queryseq', //仅为示例，并非真实接口地址。
							data: res.result,
							method: "POST",
							success: (res) => {
								var result = res.data;
								console.log(res.data);
								_this.tableSeqData = res.data;
								_this.showSeq = true;
							},
							fail: (error) => {
								uni.showModal({
									showCancel: false,
									content: "查询失败"
								})
							}
						});
					}
				})
			}

		},
		mounted() {
			uni.getStorage({
				key: "address",
				success: (res) => {
					this.address = res.data;
				}
			})
			uni.getStorage({
				key: "workno",
				success: (res) => {
					this.workno = res.data;
					// console.log(this.workno)
				}
			})
			uni.getStorage({
				key: "name",
				success: (res) => {
					this.name = res.data;
					// console.log(this.workno)
				}
			})

		}
	}
</script>

<style>
	button {
		font-size: 40rpx;
	}

	.imgbox {
		position: relative;
		overflow: hidden;
		/* padding-bottom: 100%; */
		/* outline: 1px solid green; */
	}

	.imgbox img {
		position: absolute;
		width: 15%;
		height: 100%;
		left: 30%;
	}
</style>
