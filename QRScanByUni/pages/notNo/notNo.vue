<template>
	<view class="container">
		<view class="uni-padding-wrap uni-common-mt">
			<view class="uni-flex uni-row" style="justify-content: center;">
				<input class="uni-input" v-model="facno" placeholder="款号" style="flex:1.75; border: 1px solid #e6e6e7; border-radius: 5px;" />
				<view style="flex:0.25;">
					<button type="scan" @click="facscan" class="imgbox">
						<!-- app -->
						<img src="static/sao.png" alt="" class="img">

						<!-- 小程序 -->
						<!-- <img src="../../static/sao.png" alt="" class="img"> -->
					</button>
				</view>
			</view>
			<view style="margin-top: 1%;">
				<input class="uni-input" :value="info" @click="selectItem" @focus="getItem" placeholder="工序" style=" border: 1px solid #e6e6e7; border-radius: 5px;" />
				<multiple-select v-model="show" :data="list" @confirm="confirm"></multiple-select>
			</view>
		</view>
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
				<button type="scan2" @click="scan" class="imgbox2">
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
					<td><button type="primary" @click="getDetail(index)" style="font-size: 25rpx;">明细</button></td>
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
					<td style='width: 40px;'>{{item.facno}}</td>
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
					<tr v-for="(item, index) in tableTodayDetail">
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
	// import ldSelect from '@/components/ld-select/ld-select.vue'
	import multipleSelect from '@/components/momo-multipleSelect/momo-multipleSelect.vue';
	import vTable from "@/components/no-bad-table/table.vue";
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue'
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue'
	export default {
		components: {
			multipleSelect,
			vTable,
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
				show: false, //是否显示 - 双向绑定
				info: "",
				list: [], //工序列表
				facno: "",
				address: "",
				disabled: true,
				workno: "",
				name: "",
				dateStart: currentDateStart,
				dateEnd: currentDateEnd,
				//自定义操作列
				tableData: [],
				tableDataDetail: [],
				tableTodayDetail: [],
				// 汇总显示
				showAll: false,
				showDetail: false,
				showTodayDetail: false,
				qrParam: {}
			}
		},
		methods: {
			selectItem() {
				this.show = true;
			},
			confirm(data) {
				console.log(data);
				this.info = data.map((el) => el.value).join("+");
			},
			scan() {
				var _this = this;
				uni.scanCode({
					success: function(res) {
						console.log(res.result)
						var str = res.result.split("@");
						// _this.facno = str[1];
						if (_this.facno == str[1]) {
							_this.addinfo(res.result);
						} else {
							uni.showModal({
								showCancel: false,
								content: "款号不正确"
							})
						}
					}
				})
			},
			addinfo(res) {
				var _this = this;
				var qrParam = {
					"strs": res,
					"jobNum": this.workno,
					"name": this.name,
					"facnostr": this.info
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
			facscan() {
				var _this = this;
				uni.scanCode({
					scanType: ['qrCode', 'barCode', 'datamatrix', 'pdf417'],
					success: function(res) {
						console.log(res.result);
						var str = res.result.split("@");
						_this.facno = str[1];
						console.log(res.charSet);
					}
				})
			},
			selectToday() {
				var _this = this;
				this.showAll = false;
				this.showDetail = false;
				this.showTodayDetail = true;
				console.log(this.workno);
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
							content: "查询失败"
						})
					}
				});
			},
			selectAll() {
				console.log(this.dateStart + "-" + this.dateEnd + ' - ' + this.workno + '-' + this.address);
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
			getItem() {
				var _this = this;
				this.list = [];
				uni.request({
					url: 'http://' + _this.address + '/getfacno', //仅为示例，并非真实接口地址。
					data: {
						facno: _this.facno
					},
					method: "GET",
					success: (res) => {
						var result = res.data;
						console.log(res.data);

						for (var a = 0; a < result.length; a++) {
							_this.list.push({
								label: result[a].item + ' - ' + result[a].descn,
								value: result[a].item
							})
						}
						// this.name = res.data;
						console.log(_this.list)
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
	.text-area {
		width: 100%;
		margin-top: 5vh;
		display: flex;
		justify-content: center;
		font-size: 28rpx;
		box-sizing: border-box;
		padding: 20rpx;
	}

	.value {
		color: #2088f9;
	}

	.title {
		text-align: center;
		font-size: 36rpx;
		color: #2088f9;
		margin-top: 20vh;
	}

	uni-button[type=scan] {
		background-color: #6C757D;
	}

	uni-button[type=scan] {
		padding-left: 0px;
		text-align: initial;
	}

	.imgbox {
		position: relative;
		overflow: hidden;
		padding-bottom: 100%;
		/* outline: 1px solid green; */
	}

	.imgbox img {
		position: absolute;
		left: 0px;
		width: 85%;
		height: 85%;
		margin: 8%;
	}

	button {

		font-size: 28rpx;
	}

	.imgbox2 {
		position: relative;
		overflow: hidden;
		/* padding-bottom: 100%; */
		/* outline: 1px solid green; */
	}

	.imgbox2 img {
		position: absolute;
		width: 25%;
		height: 90%;
		left: 15%;
		top: 5%;
	}

	uni-button[type=scan2] {
		background-color: #6C757D;
	}
</style>
