<template>
	<view class="container">
		<view class="uni-flex uni-row" style="margin-top: 1%;">
			<view class="uni-list" style="flex: 1;">
				<view class="uni-list-cell">
					<view class="uni-list-cell-db">
						<picker mode="date" :value="dateStart" @change="bindDateChangeStart">
							<view class="uni-input">{{dateStart}}</view>
						</picker>
					</view>
				</view>
			</view>
			<view style="flex: 0.2;text-align: center;background-color: #E9ECEF; line-height: 1.8;padding-top: 2.3%;">至</view>
			<view class="uni-list" style="flex: 1;">
				<view class="uni-list-cell">
					<view class="uni-list-cell-db">
						<picker mode="date" :value="dateEnd" @change="bindDateChangeEnd">
							<view class="uni-input">{{dateEnd}}</view>
						</picker>
					</view>
				</view>
			</view>
		</view>
		<view class="uni-flex uni-row" style="margin-top: 1%;">
			<view style="flex: 1;">
				<button type="primary" @click="selectAll">汇总查询</button>
			</view>
			<view style="flex: 1;">
				<button type="primary" @click="selectToday">当日扫码明细</button>

			</view>
			<view style="flex: 1;">
				<button type="scan" @click="scan" class="imgbox">
					<!-- app -->
					<img src="static/sao.png" alt="" class="img">
					
					<!-- 小程序 -->
					<!-- <img src="../../static/sao.png" alt="" class="img"> -->
					<span style="margin-left: 20%;color: white;">扫一扫</span></button>
			</view>
		</view>
		<view style="margin-top: 1%;">
			<table style="font-size: 12px; " v-show="showAll">
				<thead>
					<tr style="border: 1px solid black; text-align: center;">
						<th>制单</th>
						<th>款号</th>
						<th>工序</th>
						<th>总量</th>
						<th>时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tr v-for="(item, index) in tableData" style="border-bottom: 1px solid black;">
					<td style='width: 75px;'>{{item.prdno}}</td>
					<td style='width: 50px;'>{{item.facno}}</td>
					<td style='width: 35px;'>{{item.item}}</td>
					<td style='width: 35px;'>{{item.sumqty}}</td>
					<td style='width: 75px;'>{{item.recdate1}}</td>
					<td ><button type="primary" @click="getDetail(index)" style="font-size: 25rpx;">明细</button></td>
				</tr>
			</table>
		</view>
		<view style="margin-top: 1%;">
			<table style="font-size: 12px; " v-show="showDetail">
				<thead>
					<tr style="border: 1px solid black; text-align: center;">
						<th>制单</th>
						<th>款号</th>
						<th>床号</th>
						<th>扎号</th>
						<th>工序</th>
						<th>数量</th>
					</tr>
				</thead>
				<tr v-for="(item, index) in tableDataDetail" style="border-bottom: 1px solid black;">
					<td style='width: 75px;'>{{item.prdno}}</td>
					<td style='width: 50px;'>{{item.facno}}</td>
					<td style='width: 35px;'>{{item.bedno}}</td>
					<td style='width: 35px;'>{{item.seq}}</td>
					<td style='width: 100px;'>{{item.prdmoedl.descn}}</td>
					<td style='width: 25px;'>{{item.qty}}</td>
				</tr>
			</table>
		</view>
		<view style="margin-top: 1%;">
			<table style="font-size: 12px;" v-show="showTodayDetail">
				<thead>
					<tr style=" text-align: center;">
						<th>序</th>
						<th>制单</th>
						<th>款号</th>
						<th>床</th>
						<th>扎</th>
						<th>工序</th>
						<th>数量</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="(item, index) in tableTodayDetail" >
						<td style='width: 25px;'>{{index+1}}</td>
						<td style='width: 90px;'>{{item.prdno}}</td>
						<td style='width: 40px;'>{{item.facno}}</td>
						<td style='width: 15px;'>{{item.bedno}}</td>
						<td style='width: 15px;'>{{item.seq}}</td>
						<td style='width: 120px;'>{{item.item}} - {{item.prdmoedl.descn}}</td>
						<td style='width: 25px;'>{{item.qty}}</td>
					</tr>
				</tbody>
			</table>
		</view>
	</view>
</template>

<script>
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue';
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue';
	export default {
		components: {
			uniPopup,
			uniPopupMessage,
			uniPopupDialog
		},
		data() {
			const currentDateStart = this.getDateStart({
				format: true
			})
			const currentDateEnd = this.getDateEnd({
				format: true
			})
			return {
				address: "",
				workno: "",
				name: "",
				dateStart: currentDateStart,
				dateEnd: currentDateEnd,
				tableData: [],
				tableDataDetail: [],
				tableTodayDetail: [],
				// 汇总显示
				showAll: false,
				showDetail: false,
				showTodayDetail: false
			}
		},
		methods: {
			scan() {
				var _this = this;
				uni.scanCode({
					success: function(res) {
						_this.addinfo(res.result);
					}
				})
			},
			addinfo(res) {
				var _this = this;
				var qrParam = {
					"strs": res,
					"jobNum": this.workno
					// "name": this.name,
					// "facnostr": this.info
				}
				console.log(qrParam);
				uni.request({
					url: 'http://' + _this.address + '/home',
					data: qrParam,
					method: "POST",
					success: (res) => {
						uni.showModal({
							showCancel: false,
							content: "扫描成功"
						})
					},
					fail: (error) => {
						uni.showModal({
							showCancel: false,
							content: "扫描失败"
						})
					}
				});
			},
			selectToday() {
				var _this = this;
				this.showAll = false;
				this.showDetail = false;
				this.showTodayDetail = true;
				uni.request({
					url: 'http://' + _this.address + '/get', //仅为示例，并非真实接口地址。
					data: _this.workno,
					method: "POST",
					success: (res) => {
						var result = res.data;
						console.log(res.data);
						_this.tableTodayDetail = result;
					},
					fail: (error) => {
						uni.showModal({
							showCancel: false,
							content: error
						})
					}
				});
			},
			selectAll() {
				var _this = this;
				this.showAll = true;
				this.showDetail = false;
				this.showTodayDetail = false;
				uni.request({
					url: 'http://' + _this.address + '/getBydate', //仅为示例，并非真实接口地址。
					data: {
						"startDate": _this.dateStart,
						"endDate": _this.dateEnd,
						"jobNum": _this.workno
					},
					method: "POST",
					success: (res) => {
						var result = res.data;
						console.log(res.data);
						_this.tableData = result;
					},
					fail: (error) => {
						uni.showModal({
							showCancel: false,
							content: error
						})
					}
				});
			},
			getDetail(e) {
				console.log(e);
				var row = this.tableData[e];
				// console.log(row);
				var _this = this;
				var qrparam = {
					"item": row.item,
					"facno": row.facno,
					"prdno": row.prdno,
					"workno": row.workno,
					"recdate1": row.recdate1
				}
				this.showDetail = true;
				this.showAll = false;
				this.showTodayDetail = false;
				uni.request({
					url: 'http://' + _this.address + '/getmingxi', //仅为示例，并非真实接口地址。
					// data:JSON.stringify(qrparam),
					data: qrparam,
					method: "POST",
					success: (res) => {
						var result = res.data;
						console.log(res.data);
						this.tableDataDetail = res.data;

					},
					fail: (error) => {
						uni.showModal({
							showCancel: false,
							content: error
						})
					}
				});


			},
			bindDateChangeStart(e) {
				this.dateStart = e.target.value
			},
			bindDateChangeEnd(e) {
				this.dateEnd = e.target.value
			},
			getDateStart(type) {
				const date = new Date();
				let year = date.getFullYear();
				let month = date.getMonth() + 1;
				let day = date.getDate();

				if (type === 'start') {
					year = year - 60;
				} else if (type === 'end') {
					year = year + 2;
				}
				month = month > 9 ? month : '0' + month;
				day = day > 9 ? day : '0' + day;
				return `${year}-${month}-${day}`;
			},
			getDateEnd(type) {
				const date = new Date();
				let year = date.getFullYear();
				let month = date.getMonth() + 1;
				let day = date.getDate();

				if (type === 'start') {
					year = year - 60;
				} else if (type === 'end') {
					year = year + 2;
				}
				month = month > 9 ? month : '0' + month;
				day = day > 9 ? day : '0' + day;
				return `${year}-${month}-${day}`;
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
		font-size: 30rpx;
	}

	.imgbox {
		position: relative;
		overflow: hidden;
		/* padding-bottom: 100%; */
		/* outline: 1px solid green; */
	}

	.imgbox img {
		position: absolute;
		width: 30%;
		height: 100%;
		left: 10%;
	}

	uni-button[type=scan] {
		background-color: #6C757D;
	}
</style>
