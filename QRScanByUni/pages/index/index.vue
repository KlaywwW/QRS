<template>
	<view class="container" style="margin-bottom: 2px;">
		<view class="uni-padding-wrap uni-common-mt">
			<view class="uni-center" style="background:#FFFFFF; font-size:0;">
				<image class="image" src="../../static/s.png" style=" width:500rpx;height: 400rpx;"></image>
			</view>
			<view class="uni-flex uni-row" style="margin-top: 1%;">
				<input class="uni-input" :disabled="disabled" v-model="address" placeholder="地址" style="flex:1; border: 1px solid #e6e6e7; border-radius: 5px;" />

			</view>
			<view class="uni-flex uni-row" style="margin-top: 1%;">
				<input class="uni-input" :disabled="disabled" v-model="workno" @blur="getName" placeholder="工号" style="flex:1; border: 1px solid #e6e6e7; border-radius: 5px;" />
				<input class="uni-input" :disabled="disabled" v-model="name" placeholder="姓名" style="flex:1; border: 1px solid #e6e6e7; border-radius: 5px;" />
			</view>
			<view class="uni-flex uni-row" style="margin-top: 1%;">
				<div style="flex:1">
					<button type="primary" @click="save" style="margin: 0px;"> <span style="font-size: 40rpx; color: white;">保存</span></button>
				</div>
				<div style="flex:1">
					<button class="button" type="primary" @click="confirmDialog" style="margin: 0px;"><text class="button-text" style="font-size: 40rpx; color: white;">修改</text></button>
					<!-- <button type="primary" @click="update" style="margin: 0px;"> <span style="font-size: 40rpx; color: white;">修改</span></button> -->
				</div>
			</view>
			<button type="info" @click="notNo">无工序扫码</button>
			<button type="info" @click="haveNo">有工序扫码</button>
			<button type="primary" @click="seqQery">扎号信息</button>
			<button type="primary" @click="adminPanel">扫码管理面板</button>
			<!-- <button type="primary" @click="inWarehouse">进仓</button>
			<button type="primary" @click="outWarehouse">出仓</button> -->
			<uni-popup id="dialogInput" ref="dialogInput" type="dialog">
				<uni-popup-dialog mode="input" title="请输入密码" placeholder="密码" @confirm="dialogInputConfirm"></uni-popup-dialog>
			</uni-popup>
		</view>
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
				address: "",
				workno: "",
				name: "",
				disabled: true
			}
		},
		mounted(){
			uni.getStorage({
				key: "address",
				success: (res) => {
					this.address=res.data;
				}
			})
			uni.getStorage({
				key: "workno",
				success: (res) => {
					this.workno=res.data;
				}
			})
			uni.getStorage({
				key: "name",
				success: (res) => {
					this.name=res.data;
				}
			})
		},
		methods: {
			getName(){
				uni.request({
				    url: "http://"+this.address+"/getname", //仅为示例，并非真实接口地址。
					header:{
						"Content-Type":"application/json"
					},
				    data: {
				        workno: this.workno
				    },
					method:"GET",
				    success: (res) => {
				        console.log(res);
				        this.name = res.data;
				    },
					fail: (error) => {
						console.log(error);
					}
				});
			},
			dialogInputConfirm(done, val) {
				console.log(val);
				if(val == "1203a"){
					this.disabled=false;
				}else{
					uni.showModal({
						showCancel:false,
						content: "密码错误"
					})
				}
				done()
			},
			dialogClose(done) {
				this.msgType = 'info'
				this.message = '点击了对话框的取消按钮'
				this.$refs.popupMessage.open()
				// 需要执行 done 才能关闭对话框
			},
			confirmDialog() {
				// 输入框的值
				this.$refs.dialogInput.open();
			},
			save() {
				console.log(this.address + this.workno + this.name)
				var url = "http://" + this.address;
				uni.setStorage({
					key: "address",
					data: this.address
				})
				uni.setStorage({
					key: "workno",
					data: this.workno
				})
				uni.setStorage({
					key: "name",
					data: this.name
				})
				uni.showModal({
					showCancel:false,
					content: "保存成功"
				})
				
			},
			
			notNo() {
				uni.navigateTo({
					url: '../notNo/notNo'
				})
			},
			haveNo() {
				uni.navigateTo({
					url: '../haveNo/haveNo'
				})
			},
			seqQery() {
				uni.navigateTo({
					url: '../seqquery/seqquery'
				})
			},
			adminPanel() {
				uni.navigateTo({
					url: '../adminPanel/adminPanel'
				})
			},
			inWarehouse() {
				uni.navigateTo({
					url: '../inWarehouse/inWarehouse'
				})
			},
			outWarehouse() {
				uni.navigateTo({
					url: '../outWarehouse/outWarehouse'
				})
			}
		}
	}
</script>

<style>
	uni-button[type=info] {
		color: #fff;
		background-color: #138496;
	}

	button {
		margin-top: 5px;
	}
</style>
