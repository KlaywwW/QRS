<template>
	<view class="container">
		<view class="uni-padding-wrap uni-common-mt">
			<view class="uni-list" style="border: 1px solid #e6e6e7; border-radius: 5px;margin-top: 1%;">
				<view class="uni-list-cell">
					<view class="uni-list-cell-left" style="color: #808080;">
						制单
					</view>
					<view class="uni-list-cell-db">
						<picker @change="bindPickerChange" :value="index" :range="array">
							<view class="uni-input">{{array[index]}}</view>
						</picker>
					</view>
				</view>
				<view class="uni-list-cell">
					<view class="uni-list-cell-left" style="color: #808080;">
						款号
					</view>
					<view class="uni-list-cell-db">
						<input class="uni-input" v-model="facno" focus/>
					</view>
				</view>
				<view class="uni-list-cell">
					<view class="uni-list-cell-left" style="color: #808080;">
						数量
					</view>
					<view class="uni-list-cell-db">
						<!-- <input class="uni-input" v-model="facno" focus /> -->
						<input class="uni-input" v-model="prdnum"/>
					</view>
				</view>
			</view>
		</view>
		<view style="margin-top: 1%;">
			<table style="font-size: 12px; " v-show="showAdmin" border-bottom="1">
				<thead>
					<tr style="border: 1px solid black; text-align: center;">
						<th>制单</th>
						<th>款号</th>
						<th>工序</th>
						<th>生产数</th>
						<th>生产率</th>
					</tr>
				</thead>
				<tr v-for="(item, index) in tableDataAdmin">
					<td style='width: 70px;'>{{item.prdno}}</td>
					<td style='width: 40px;'>{{item.facno}}</td>
					<td style='width: 115px;'>{{item.prdmoedl.item}} - {{item.prdmoedl.descn}}</td>
					<td style='width: 40px;'>{{item.qty}}</td>
					<td style='width: 75px;'>{{((item.qty/item.prdn.prdnum)*100).toFixed(2)}}%</td>
				</tr>
			</table>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				showAdmin: false,
				prdno: '',
				facno: '',
				prdnum: '',
				title: 'picker',
				array: [],
				index: 0,
				address: '',
				workno: '',
				name: '',
				data: {},
				tableDataAdmin: []
			}
		},
		methods: {
			bindPickerChange: function(e) {
				var _this = this;
				this.showAdmin = true;
				console.log('picker发送选择改变，携带值为', e.target.value)
				this.index = e.target.value
				console.log(e.target);
				this.facno = this.data[e.target.value].facno;
				this.prdnum = this.data[e.target.value].prdnum;
				this.prdno = this.data[e.target.value].prdno;
				uni.request({
					url: 'http://' + _this.address + '/getfac', //仅为示例，并非真实接口地址。
					data: _this.prdno,
					method: "POST",
					success: (result) => {
						var result = result.data;
						console.log(result);
						_this.tableDataAdmin = result;
					},
					fail: (error) => {
						uni.showModal({
							showCancel: false,
							content: error
						})
					}
				});
			}
		},
		mounted() {
			var _this = this;
			uni.getStorage({
				key: "address",
				success: (res) => {
					this.address = res.data;
					uni.request({
						url: 'http://' + res.data + '/getprd', //仅为示例，并非真实接口地址。
						// data:JSON.stringify(qrparam),
						method: "GET",
						success: (result) => {
							var result = result.data;
							console.log(result);
							_this.data = result;
							var newArray = [];
							for (var i = 0; i < result.length; i++) {
								newArray[i] = result[i].prdno;
							}
							_this.array = newArray;

						},
						fail: (error) => {
							uni.showModal({
								showCancel: false,
								content: error
							})
						}
					});
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

</style>
