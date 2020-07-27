<template>
	<view class="container">
		<view class="uni-padding-wrap uni-common-mt">
			<view class="uni-flex uni-row">
				<input class="uni-input" v-model="facno" focus placeholder="采购单号" style="flex:1.75; border: 1px solid #e6e6e7; border-radius: 5px;" />
				<view style="flex:0.25;">
					<button type="scan" @click="facscan" class="imgbox"><img src="../../common/img/sao2.svg" alt="" class="img"></button>
				</view>
			</view>
			<input class="uni-input" v-model="facno" focus placeholder="进仓单号" style="flex:1.75; border: 1px solid #e6e6e7; border-radius: 5px;" />
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>

<style>
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
		width: 85%;
		height: 85%;
		margin: 8%;
	}
</style>
