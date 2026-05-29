import store from '@/store/index';
import $ from 'jquery';
import { CheckInputFloat, CheckInputInt } from '@/utils/index.js';
export const hasPerm = {
	install: (app) => {
		app.directive('hasPerm', {
			mounted(el, binding) {
				let flag = false;
				var permissions = store.state.permissionStore.permission;
				if (permissions) {
					if (binding.value) {
						const value = binding.value
						flag = permissions.has(value);
					} else { flag = false; }
				} else {
					flag = true;
				}

				if (!flag) {
					if (!el.parentNode) {
						el.style.display = 'none'
					} else {
						el.parentNode.removeChild(el)
					}
				}
			}
		});
	}
}
export const hasPermi = {
	install: (app) => {
		app.directive('hasPermi', {
			mounted(el, binding) {
				let flag = false;
				var permissions = Array.from(store.state.permissionStore.permission);
				if (permissions) {
					if (binding.value && binding.value instanceof Array && binding.value.length > 0) {
						const permissionFlag = binding.value;
						const all_permission = "*:*:*";
						const hasPermissions = permissions.some(permission => {
							return all_permission === permission || permissionFlag.includes(permission)
						})
						flag = hasPermissions;
					} else { flag = false; }
				} else {
					flag = true;
				}
				if (!flag) {
					if (!el.parentNode) {
						el.style.display = 'none'
					} else {
						el.parentNode.removeChild(el)
					}
				}
			}
		});
	}
}
export const dataType = {
	install: (app) => {
		app.directive('dataType', {
			mounted(el, binding) {
				var $input = $(el).find("input");
				if ($input != undefined && $input.length > 0) {
					var input = ($(el).find("input"))[0];
					const value = binding.arg ? binding.arg : binding.value;
					if (value == "float") {
						input.addEventListener('input', function () {
							this.value = CheckInputFloat(this.value);
						}, true);
						// input.oninput=function(){
						// 	   this.value=CheckInputFloat(this.value)
						// }
					}
					if (value == "int") {
						input.addEventListener('input', function () {
							this.value = CheckInputInt(this.value);
						}, true);
						// input.oninput=function(){
						// 	   this.value=CheckInputInt(this.value)
						// }
					}
				}
			}
		});
	}
}