{
	"$schema": "https://schemas.amazon.com/selling-partners/definitions/product-types/meta-schema/v1",
	"$id": "https://schemas.amazon.com/selling-partners/definitions/product-types/schema/v1/HAIRBAND",
	"$comment": "Amazon product type definition for HAIRBAND product type",
	"$defs": {
		"marketplace_id": {
			"default": "ATVPDKIKX0DER",
			"editable": false,
			"hidden": true,
			"examples": ["Amazon.com"],
			"type": "string",
			"anyOf": [{
				"type": "string"
			}, {
				"type": "string",
				"enum": ["ATVPDKIKX0DER"],
				"enumNames": ["Amazon.com"]
			}]
		},
		"language_tag": {
			"default": "en_US",
			"editable": false,
			"hidden": true,
			"examples": ["英语 (美国)"],
			"type": "string",
			"anyOf": [{
				"type": "string"
			}, {
				"type": "string",
				"enum": ["en_US"],
				"enumNames": ["英语 (美国)"]
			}]
		}
	},
	"type": "object",
	"required": ["brand", "bullet_point", "country_of_origin", "fabric_type", "item_name", "item_type_keyword",
		"product_description", "supplier_declared_dg_hz_regulation"
	],
	"properties": {
		"item_name": {
			"title": "标题",
			"description": "提供可能面向顾客的产品标题",
			"examples": ["Adidas Blue 运动鞋"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "商品名称",
						"description": "提供可能面向顾客的产品标题",
						"editable": true,
						"hidden": false,
						"examples": ["托斯卡纳葡萄园手绘画布"],
						"type": "string",
						"minLength": 0,
						"maxLength": 200
					}
				},
				"additionalProperties": false
			}
		},
		"brand": {
			"title": "品牌",
			"description": "商品的品牌或制造商。",
			"examples": ["OLAY 玉兰油"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "品牌名",
						"description": "提供产品品牌名",
						"editable": false,
						"hidden": false,
						"examples": ["索尼"],
						"type": "string",
						"minLength": 1,
						"maxLength": 100,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["KUUQA"],
							"enumNames": ["KUUQA"]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"externally_assigned_product_identifier": {
			"title": "外部产品 ID",
			"description": "请提供外部 ID（条形码）类型以及用于识别产品相应的值",
			"examples": ["714532191586"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "type"],
			"items": {
				"type": "object",
				"required": ["type", "value"],
				"properties": {
					"type": {
						"title": "外部产品 ID 类型",
						"description": "选择用于识别此产品的外部 ID（条形码）类型",
						"editable": false,
						"hidden": false,
						"examples": ["UPC"],
						"type": "string",
						"enum": ["ean", "gtin", "upc"],
						"enumNames": ["EAN", "GTIN", "UPC"]
					},
					"value": {
						"title": "外部产品 ID",
						"description": "根据所选类型提供相应的外部产品 ID 值",
						"editable": false,
						"hidden": false,
						"examples": ["714532191586"],
						"type": "string"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"merchant_suggested_asin": {
			"title": "商家建议的 ASIN",
			"description": "为您的商品提供 ASIN（如果存在）。如果未提供数值，系统将尝试根据外部商品 ID 进行匹配。",
			"examples": ["B007KQBXN0"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "value"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "商家建议的 ASIN",
						"description": "为您的商品提供 ASIN（如果存在）。如果未提供数值，系统将尝试根据外部商品 ID 进行匹配。",
						"editable": false,
						"hidden": false,
						"examples": ["B007KQBXN0"],
						"type": "string",
						"minLength": 10,
						"maxLength": 10,
						"maxUtf8ByteLength": 40
					}
				},
				"additionalProperties": false
			}
		},
		"supplier_declared_has_product_identifier_exemption": {
			"title": "從供應商聲明的外部標識符豁免。",
			"description": "請指定產品是否免除供應商聲明的外部產品標識碼。",
			"examples": ["是，没有"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "從供應商聲明的外部標識符豁免。",
						"description": "請指定產品是否免除供應商聲明的外部產品標識碼。",
						"editable": true,
						"hidden": false,
						"examples": ["是，没有"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					}
				},
				"additionalProperties": false
			}
		},
		"item_type_keyword": {
			"title": "产品类型",
			"description": "商品类型关键字用于将新 ASIN 放置在图表中的适当位置。选择最具体、最准确的术语以获得最佳放置位置。",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品类型关键字",
						"description": "商品类型关键字用于将新 ASIN 放置在图表中的适当位置。选择最具体、最准确的术语以获得最佳放置位置。",
						"editable": false,
						"hidden": false,
						"examples": ["随身行李"],
						"type": "string",
						"maxLength": 20090
					}
				},
				"additionalProperties": false
			}
		},
		"package_level": {
			"title": "包装级别",
			"description": "提供商品的包装等级。如果未提供包装等级或不适用，请选择“单位”。为每个包装等级提供一个“单位”商品。",
			"examples": ["单位、盒子"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "包装级别",
						"description": "提供商品的包装等级。如果未提供包装等级或不适用，请选择“单位”。为每个包装等级提供一个“单位”商品。",
						"editable": true,
						"hidden": false,
						"examples": ["单位、盒子"],
						"type": "string",
						"enum": ["case", "unit", "pallet"],
						"enumNames": ["Case", "单元", "货盘"]
					}
				},
				"additionalProperties": false
			}
		},
		"package_contains_sku": {
			"title": "包裹包含 SKU",
			"description": "提供下一级包裹中包含的子商品的 SKU 和数量。",
			"examples": ["SKU： ABC123，数量： 1"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "sku"],
			"items": {
				"type": "object",
				"required": ["quantity", "sku"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"quantity": {
						"title": "包裹包含 SKU 数量",
						"description": "提供包裹级别中标识的每件商品、箱子或托盘的数量。",
						"editable": true,
						"hidden": false,
						"type": "integer",
						"minimum": 1,
						"maximum": 250
					},
					"sku": {
						"title": "包裹包含 SKU 编码",
						"description": "提供包裹级别中标识的每件商品、箱子或托盘的 SKU 编码。",
						"editable": true,
						"hidden": false,
						"examples": ["ABC123"],
						"type": "string",
						"maxLength": 100
					}
				},
				"additionalProperties": false
			}
		},
		"target_audience_keyword": {
			"title": "适用人群",
			"description": "商品面向的主要适用人群。",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 53,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "目标受众关键词",
						"description": "给定说明产品目标受众（即最终用户）的术语",
						"editable": true,
						"hidden": false,
						"examples": ["青少年"],
						"type": "string",
						"maxLength": 4398,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["儿童（不区分性别）", "女士", "女孩", "成人（不区分性别）", "男士", "男孩"],
							"enumNames": ["儿童（不区分性别）", "女士", "女孩", "成人（不区分性别）", "男士", "男孩"]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"age_gender_category": {
			"title": "年龄性别分类",
			"description": "说明陈列该产品的部门",
			"examples": ["男士，女士，男童女童"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "年龄性别分类",
						"description": "说明陈列该产品的部门",
						"editable": true,
						"hidden": false,
						"examples": ["男士，女士，男童女童"],
						"type": "string",
						"maxLength": 2398
					}
				},
				"additionalProperties": false
			}
		},
		"model_number": {
			"title": "型号",
			"description": "提供制造商定义的产品型号",
			"examples": ["RXZER23"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "型号",
						"description": "提供制造商定义的产品型号",
						"editable": true,
						"hidden": false,
						"examples": ["RXZER23"],
						"type": "string",
						"maxLength": 40,
						"maxUtf8ByteLength": 40
					}
				},
				"additionalProperties": false
			}
		},
		"manufacturer": {
			"title": "制造商",
			"description": "请详细说明您的商品的制造商。",
			"examples": ["李宁体育用品有限公司，佳能信息技术有限公司，安利（中国）日用品有限公司"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "制造商",
						"description": "指定产品的制造商",
						"editable": true,
						"hidden": false,
						"examples": ["耐克、宝洁"],
						"type": "string",
						"minLength": 0,
						"maxLength": 100
					}
				},
				"additionalProperties": false
			}
		},
		"unspsc_code": {
			"title": "UNSPSC 代码",
			"description": "与该项目对应的联合国指定采购/分类代码",
			"examples": ["27876546"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "UNSPSC 代码",
						"description": "与该项目对应的联合国指定采购/分类代码",
						"editable": true,
						"hidden": false,
						"examples": ["27876546"],
						"type": "string",
						"maxLength": 259
					}
				},
				"additionalProperties": false
			}
		},
		"national_stock_number": {
			"title": "国家股编号",
			"description": "为商品给出 NSN 标识",
			"examples": ["850519-S01, 783360-S01"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "国家库存品编号（National Stock Number，NSN）",
						"description": "为商品给出 NSN 标识",
						"editable": true,
						"hidden": false,
						"examples": ["850519-S01, 783360-S01"],
						"type": "string",
						"maxLength": 100
					}
				},
				"additionalProperties": false
			}
		},
		"skip_offer": {
			"title": "跳过报价",
			"description": "指明是否应跳过该报价，是否不应创建可购买的报价。值为“是”表示不会创建任何报价。",
			"examples": ["是"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "跳过报价",
						"description": "指明是否应跳过该报价，是否不应创建可购买的报价。值为“是”表示不会创建任何报价。",
						"editable": true,
						"hidden": false,
						"examples": ["是"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					}
				},
				"additionalProperties": false
			}
		},
		"fulfillment_availability": {
			"title": "配送可用性",
			"description": "对于使用 Amazon 配送服务的商家，请提供相关的物流信息。",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["fulfillment_channel_code"],
			"items": {
				"type": "object",
				"required": ["fulfillment_channel_code"],
				"properties": {
					"fulfillment_channel_code": {
						"title": "物流渠道代码",
						"description": "对于使用亚马逊物流服务的商家，指定将使用哪个物流网络。指定除 DEFAULT 以外的值将取消商家交付的产品。对于商家交付的产品，应将此字段留空。",
						"editable": true,
						"hidden": false,
						"examples": ["AMAZON_NA"],
						"type": "string",
						"enum": ["AMAZON_NA", "AMAZON_US_TBYB", "DEFAULT"],
						"enumNames": ["AMAZON_NA", "AMAZON_US_TBYB", "DEFAULT"]
					},
					"quantity": {
						"title": "数量",
						"description": "输入您可出售的商品数量。这是您当前的库存承诺（取整数）",
						"editable": true,
						"hidden": false,
						"examples": ["152"],
						"type": "integer",
						"minimum": 0
					},
					"lead_time_to_ship_max_days": {
						"title": "处理时间",
						"description": "以天为单位，提供收到商品订单到发货的时间",
						"editable": true,
						"hidden": false,
						"examples": ["5"],
						"type": "integer",
						"minimum": 0,
						"maximum": 30
					},
					"restock_date": {
						"title": "重新库存日期",
						"description": "产品补货日期",
						"editable": true,
						"hidden": false,
						"examples": ["2020-05-05"],
						"type": "string",
						"oneOf": [{
							"type": "string",
							"format": "date"
						}, {
							"type": "string",
							"format": "date-time"
						}]
					},
					"is_inventory_available": {
						"title": "始终有货",
						"description": "始终有货是除数量之外的另一种库存表示方式，表示库存永远不会耗尽。启用或禁用将打开或关闭此功能。请注意，提供时不能指定数量。",
						"editable": true,
						"hidden": false,
						"examples": ["已禁用"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["已启用", "已禁用"]
					}
				},
				"additionalProperties": false
			}
		},
		"map_policy": {
			"title": "最低广告价格显示",
			"description": "选择一个。",
			"examples": ["政策 9"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "最低广告价格显示",
						"description": "选择一个。",
						"editable": true,
						"hidden": false,
						"examples": ["政策 9"],
						"type": "string",
						"enum": ["policy_3", "policy_5"],
						"enumNames": ["Policy 3", "Policy 5"]
					}
				},
				"additionalProperties": false
			}
		},
		"purchasable_offer": {
			"title": "可购买的报价",
			"description": "提供可购买的报价信息",
			"examples": ["EUR"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "currency", "audience"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"map_price": {
						"title": "可购买报价最低广告价格",
						"description": "该属性表示产品的可购买报价最低广告价格",
						"type": "array",
						"minItems": 1,
						"items": {
							"type": "object",
							"required": [],
							"properties": {
								"schedule": {
									"title": "可购买报价最低广告定价计划",
									"description": "该属性表示产品的可购买报价最低广告定价计划",
									"type": "array",
									"minItems": 1,
									"items": {
										"type": "object",
										"required": [],
										"properties": {
											"value_with_tax": {
												"title": "最低广告价格",
												"description": "提供最低广告价格",
												"editable": true,
												"hidden": false,
												"examples": ["259.99"],
												"type": "number"
											}
										},
										"additionalProperties": false
									}
								}
							},
							"additionalProperties": false
						}
					},
					"currency": {
						"title": "貨幣",
						"description": "选择相应的货币",
						"default": "USD",
						"editable": false,
						"hidden": true,
						"examples": ["USD"],
						"type": "string",
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["USD"],
							"enumNames": ["USD"]
						}]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"our_price": {
						"title": "可购买报价我方价格",
						"description": "该属性表示对该产品的我方可购买报价",
						"type": "array",
						"minItems": 1,
						"items": {
							"type": "object",
							"required": ["schedule"],
							"properties": {
								"schedule": {
									"title": "可购买报价我方价格计划",
									"description": "该属性指示产品的可购买报价我方价格计划",
									"type": "array",
									"minItems": 1,
									"items": {
										"type": "object",
										"required": ["value_with_tax"],
										"properties": {
											"value_with_tax": {
												"title": "您的价格",
												"description": "指明向目标买家群体提供的商品基本价格",
												"editable": true,
												"hidden": false,
												"examples": ["9.00"],
												"type": "number"
											}
										},
										"additionalProperties": false
									}
								}
							},
							"additionalProperties": false
						}
					},
					"minimum_seller_allowed_price": {
						"title": "可购买报价最低卖方接受的价格",
						"description": "该属性表示产品的可购买报价最低卖方接受的价格",
						"type": "array",
						"minItems": 1,
						"items": {
							"type": "object",
							"required": [],
							"properties": {
								"schedule": {
									"title": "可购买报价最低卖方接受的价格计划",
									"description": "该属性表示产品的可购买报价、最低卖方接受价格、计划",
									"type": "array",
									"minItems": 1,
									"items": {
										"type": "object",
										"required": [],
										"properties": {
											"value_with_tax": {
												"title": "卖方允许的最低价格",
												"description": "提供卖方允许的最低价格",
												"editable": true,
												"hidden": false,
												"examples": ["259.99"],
												"type": "number"
											}
										},
										"additionalProperties": false
									}
								}
							},
							"additionalProperties": false
						}
					},
					"maximum_seller_allowed_price": {
						"title": "可购买报价最高卖方接受的价格",
						"description": "该属性表示产品的可购买报价最高卖方接受的价格",
						"type": "array",
						"minItems": 1,
						"items": {
							"type": "object",
							"required": [],
							"properties": {
								"schedule": {
									"title": "可购买报价最高卖方接受的价格计划",
									"description": "该属性表示产品的可购买报价、最高卖方接受的价格、计划",
									"type": "array",
									"minItems": 1,
									"items": {
										"type": "object",
										"required": [],
										"properties": {
											"value_with_tax": {
												"title": "卖方允许的最高价格",
												"description": "提供卖方允许的最高价格",
												"editable": true,
												"hidden": false,
												"examples": ["259.99"],
												"type": "number"
											}
										},
										"additionalProperties": false
									}
								}
							},
							"additionalProperties": false
						}
					},
					"discounted_price": {
						"title": "可购买报价折扣价格",
						"description": "该属性表示产品的可购买报价、折扣价",
						"type": "array",
						"minItems": 1,
						"items": {
							"type": "object",
							"required": ["schedule"],
							"properties": {
								"schedule": {
									"title": "可购买报价折扣价计划",
									"description": "该属性表示产品的可购买报价折扣价格计划",
									"type": "array",
									"minItems": 1,
									"items": {
										"type": "object",
										"required": ["end_at", "start_at", "value_with_tax"],
										"properties": {
											"end_at": {
												"title": "销售截止日期",
												"description": "用销售价格替代产品标准价格的最后一天；产品的标准价格将在销售截止日期的凌晨 0:00 后显示。",
												"editable": true,
												"hidden": false,
												"examples": ["2017-07-01"],
												"type": "string",
												"oneOf": [{
													"type": "string",
													"format": "date"
												}, {
													"type": "string",
													"format": "date-time"
												}]
											},
											"start_at": {
												"title": "销售开始日期",
												"description": "用销售价格替代产品标准价格的第一天；销售价格将在销售开始日期的凌晨 0:00 后显示。",
												"editable": true,
												"hidden": false,
												"examples": ["2017-06-30"],
												"type": "string",
												"oneOf": [{
													"type": "string",
													"format": "date"
												}, {
													"type": "string",
													"format": "date-time"
												}]
											},
											"value_with_tax": {
												"title": "销售价格",
												"description": "您提供的待售产品的价格。",
												"editable": true,
												"hidden": false,
												"examples": ["219.99"],
												"type": "number"
											}
										},
										"additionalProperties": false
									}
								}
							},
							"additionalProperties": false
						}
					},
					"start_at": {
						"title": "可购买报价开始于",
						"description": "该属性表示产品的可购买报价、起始时间",
						"type": "object",
						"required": [],
						"properties": {
							"value": {
								"title": "提供发布日期",
								"description": "您的价格的开始日期",
								"editable": true,
								"hidden": false,
								"examples": ["2017-06-30"],
								"type": "string",
								"oneOf": [{
									"type": "string",
									"format": "date"
								}, {
									"type": "string",
									"format": "date-time"
								}]
							}
						},
						"additionalProperties": false
					},
					"end_at": {
						"title": "可购买报价结束于",
						"description": "该属性表示产品的可购买报价结束于",
						"type": "object",
						"required": [],
						"properties": {
							"value": {
								"title": "停售日期",
								"description": "您的价格的截止日期",
								"editable": true,
								"hidden": false,
								"examples": ["2017-07-01"],
								"type": "string",
								"oneOf": [{
									"type": "string",
									"format": "date"
								}, {
									"type": "string",
									"format": "date-time"
								}]
							}
						},
						"additionalProperties": false
					},
					"audience": {
						"editable": false,
						"hidden": true,
						"type": "string",
						"enum": ["B2B", "ALL"],
						"enumNames": ["亚马逊企业购 (B2B)", "在亚马逊上出售"]
					},
					"quantity_discount_plan": {
						"title": "可购买的企业优惠批量折扣计划",
						"description": "为商品企业价格提供并定义批量折扣计划。",
						"examples": ["计划批量价格类型： 固定；计划等级批量下限： 5；计划等级批量价格： 10"],
						"type": "array",
						"minItems": 1,
						"items": {
							"type": "object",
							"required": ["schedule"],
							"properties": {
								"schedule": {
									"title": "计划",
									"description": "说明用于定义安排的批量折扣计划的详细信息，例如日期、定价类型。",
									"examples": ["批量价格起始日期： 2024 年 6 月 30 日；批量价格结束日期： 2024 年 7 月 1 日"],
									"type": "array",
									"minItems": 1,
									"items": {
										"type": "object",
										"required": ["discount_type", "levels"],
										"properties": {
											"discount_type": {
												"title": "批发价格类型",
												"description": "说明批量价格类型是否为“固定”、每个批量阈值的当地货币价格或商品企业价格的百分比。",
												"editable": true,
												"hidden": false,
												"examples": ["固定、百分比"],
												"type": "string",
												"enum": ["fixed", "percent"],
												"enumNames": ["fixed", "percent"]
											},
											"levels": {
												"title": "等级",
												"description": "批量折扣计划日程等级",
												"examples": ["批量折扣计划日程等级"],
												"type": "array",
												"minItems": 1,
												"maxItems": 5,
												"items": {
													"type": "object",
													"required": ["lower_bound", "value"],
													"properties": {
														"lower_bound": {
															"title": "批量阈值（下限）",
															"description": "说明获得折扣所需的最低购买量。如果达到批量阈值，将适用于所有单位。",
															"editable": true,
															"hidden": false,
															"examples": ["5、10"],
															"type": "integer",
															"minimum": 0
														},
														"value": {
															"title": "批发价格（固定价格/百分比折扣）",
															"description": "说明每个批量阈值的批发价格，或根据每个批量阈值的企业价格提供的折扣百分比。",
															"editable": true,
															"hidden": false,
															"examples": ["10、20"],
															"type": "number",
															"minimum": 0
														}
													},
													"additionalProperties": false
												}
											}
										},
										"additionalProperties": false
									}
								}
							},
							"additionalProperties": false
						}
					}
				},
				"additionalProperties": false
			}
		},
		"condition_type": {
			"title": "提供条件类型",
			"description": "提供产品的实际条件类型",
			"examples": ["全新"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "提供条件类型",
						"description": "提供产品的实际条件类型",
						"editable": false,
						"hidden": false,
						"examples": ["全新"],
						"type": "string",
						"enum": ["new_new"],
						"enumNames": ["新"]
					}
				},
				"additionalProperties": false
			}
		},
		"condition_note": {
			"title": "新旧程度说明",
			"description": "用于具体说明该商品状态的描述性文本。",
			"examples": ["左侧面板有小凹痕。"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "出价条件说明",
						"description": "提供说明项目实际情况的描述性文字",
						"editable": true,
						"hidden": false,
						"examples": ["左侧面板有小凹痕。"],
						"type": "string",
						"maxLength": 2204
					}
				},
				"additionalProperties": false
			}
		},
		"list_price": {
			"title": "不含税价目表",
			"description": "给出不含税的产品标价。 标价通常是指制造商建议零售价（MSRP）、推荐零售价（RRP）或建议零售价（SRP）。这不是发行价或成本价。",
			"examples": ["67 CNY"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "currency"],
			"items": {
				"type": "object",
				"required": ["currency", "value"],
				"properties": {
					"currency": {
						"title": "价目表货币",
						"description": "选择相应的货币",
						"editable": false,
						"hidden": true,
						"examples": ["CNY"],
						"type": "string",
						"enum": ["USD"],
						"enumNames": ["USD"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "不含税价目表",
						"description": "提供商品的定价（不含税）。定价是制造商、供应商或卖家提供的商品建议零售价。这不是报价或成本价。",
						"editable": true,
						"hidden": false,
						"examples": ["67"],
						"type": "number",
						"minimum": 0,
						"maxLength": 20,
						"multipleOf": 0.01
					}
				},
				"additionalProperties": false
			}
		},
		"product_tax_code": {
			"title": "产品税码",
			"description": "输入 Amazon.com 向您提供的产品税码",
			"examples": ["A_GEN_NOTAX"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品税码",
						"description": "输入 Amazon.com 向您提供的产品税码",
						"editable": true,
						"hidden": false,
						"examples": ["A_GEN_NOTAX"],
						"type": "string",
						"maxLength": 949,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["A1_ANIMAL14", "A1_TOOLS1", "A3_SERV73", "A3_SERV74",
								"A_AUTOMOTIVE_MOTOROIL", "A_BABY_BRSTPUMP", "A_BABY_CARSEAT",
								"A_BABY_CLOTH", "A_BABY_DIAPER", "A_BOOKS_GEN", "A_BOOKS_RELIG",
								"A_BOOKS_RENTAL", "A_BUNDLE_PERDCL", "A_CLSFD_52W-4Q", "A_CLSFD_52WKLY",
								"A_CLSFD_ANNUAL", "A_CLSFD_SEMIANNL", "A_CLTH_ATHL", "A_CLTH_BATH",
								"A_CLTH_BOOKBAG", "A_CLTH_BUCKLS", "A_CLTH_COMPON", "A_CLTH_CSTUMS",
								"A_CLTH_FORMAL", "A_CLTH_FUR", "A_CLTH_GEN", "A_CLTH_HAIRACCESSORIES",
								"A_CLTH_HANDKE", "A_CLTH_HBAGS", "A_CLTH_IFUR", "A_CLTH_PROTECTIVE",
								"A_CLTH_UMBRELLA", "A_COLLECTIBLE_COIN", "A_COMP_COMPUTER",
								"A_COMP_EDUSOFT", "A_COMP_EREADER", "A_COMP_GAMPER", "A_COMP_PDA",
								"A_COMP_PERIPH", "A_COMP_PRINT", "A_COMP_PRTSUP", "A_COMP_SCANNER",
								"A_COMP_SOFTOP", "A_COMP_SOFTRC", "A_COMP_SOUNDANDVIDEOCARDS",
								"A_COMP_STORAGEMEDIA", "A_EGOODS_DIGITALAUDIOBOOKS",
								"A_EGOODS_DIGITALBOOK_RENTAL", "A_EGOODS_DIGITALBOOKS",
								"A_EGOODS_DIGITALGAMES", "A_EGOODS_DIGITALMUSIC", "A_EGOODS_DIGITALNEWS",
								"A_EGOODS_DIGITALNEWSSUBS", "A_EGOODS_DIGITALPERDCL",
								"A_EGOODS_DIGITALPERDCLSUBS", "A_EGOODS_MISC1", "A_EGOODS_ONLINEGAMINGSUBS",
								"A_EGOODS_SOFT", "A_EMERGENCY_SUPPLIES", "A_FOOD_BKTCDY50-90",
								"A_FOOD_BKTGN50-75", "A_FOOD_BKTGN76-90", "A_FOOD_CARBFLAVORWTR_NONSWEET",
								"A_FOOD_CARBSFTDK", "A_FOOD_CARBWTR", "A_FOOD_CNDY", "A_FOOD_CNDYFL",
								"A_FOOD_FLAVORWATER_NONSWEET", "A_FOOD_GEN", "A_FOOD_JUICE0-50",
								"A_FOOD_JUICE51-99", "A_FOOD_NCARBWTR", "A_FOOD_PETFOOD", "A_FOOD_WINE",
								"A_GEN_NOTAX", "A_GEN_RENTAL", "A_GEN_TAX", "A_HLTH_BABYSUPPLS",
								"A_HLTH_BANDKIT", "A_HLTH_BRAILLE", "A_HLTH_CONTACTSOLN",
								"A_HLTH_CONTRCEPV", "A_HLTH_COSMETIC", "A_HLTH_DIABSUPPLS",
								"A_HLTH_DIETSUPMT", "A_HLTH_DISPOSABLE_PACK", "A_HLTH_FACIALTISSUE",
								"A_HLTH_FAMPLANTEST", "A_HLTH_FEMHYG", "A_HLTH_FEMHYGSPRAY",
								"A_HLTH_HANDSANITIZER", "A_HLTH_HEARINGAID_BATT", "A_HLTH_INCONT",
								"A_HLTH_INCONTPADS", "A_HLTH_INCONTPADS_DISP", "A_HLTH_MEDHAIRLOSS",
								"A_HLTH_MISCDURABLEMED", "A_HLTH_MISCMOBILITY1", "A_HLTH_MISCMOBILITY2",
								"A_HLTH_MOBILITY", "A_HLTH_MONITOR", "A_HLTH_NASALSTRIP", "A_HLTH_OTCMED",
								"A_HLTH_PROSTHETIC", "A_HLTH_REMEDY", "A_HLTH_SPFCORALHYG",
								"A_HLTH_SPFCOTCMED", "A_HLTH_SUNSCRN", "A_HLTH_TENS", "A_HLTH_THRMTR",
								"A_HLTH_TISSUETOW", "A_HLTH_TOOTHPASTEFL", "A_HOUSEHOLD_LINEN",
								"A_HOUSEHOLD_PAPERPRODS", "A_HOUSEHOLD_SOLAR", "A_NEWS_104PLUS",
								"A_NEWS_12MTHLY", "A_NEWS_26BIWKLY", "A_NEWS_4QTLY", "A_NEWS_52WKLY",
								"A_PERDCL_104PLUS", "A_PERDCL_52W-4Q", "A_PERDCL_52WKLY", "A_PERDCL_ANNUAL",
								"A_PERDCL_SEMIANNL", "A_REST_COLDPREPARED", "A_REST_DELIVERY",
								"A_REST_HOTPREPARED", "A_SCHL_GRAPHCALC", "A_SCHL_INSTRUMENT",
								"A_SCHL_SUPPLS", "A_SERV_CLEAN", "A_SERV_INSTALL", "A_SPORT_ASUPPORT",
								"A_SPORT_ATHLSHOES", "A_SPORT_BIKEHLMT", "A_SPORT_MISCSPORTS1",
								"A_SPORT_PROTECTIVEGEAR", "A_SPORT_SKISUIT", "A_SPORT_WINTERSPORTBOOTS",
								"A_WARR_PARTSNSVCE"
							],
							"enumNames": ["A1_ANIMAL14", "A1_TOOLS1", "A3_SERV73", "A3_SERV74",
								"A_AUTOMOTIVE_MOTOROIL", "A_BABY_BRSTPUMP", "A_BABY_CARSEAT",
								"A_BABY_CLOTH", "A_BABY_DIAPER", "A_BOOKS_GEN", "A_BOOKS_RELIG",
								"A_BOOKS_RENTAL", "A_BUNDLE_PERDCL", "A_CLSFD_52W-4Q", "A_CLSFD_52WKLY",
								"A_CLSFD_ANNUAL", "A_CLSFD_SEMIANNL", "A_CLTH_ATHL", "A_CLTH_BATH",
								"A_CLTH_BOOKBAG", "A_CLTH_BUCKLS", "A_CLTH_COMPON", "A_CLTH_CSTUMS",
								"A_CLTH_FORMAL", "A_CLTH_FUR", "A_CLTH_GEN", "A_CLTH_HAIRACCESSORIES",
								"A_CLTH_HANDKE", "A_CLTH_HBAGS", "A_CLTH_IFUR", "A_CLTH_PROTECTIVE",
								"A_CLTH_UMBRELLA", "A_COLLECTIBLE_COIN", "A_COMP_COMPUTER",
								"A_COMP_EDUSOFT", "A_COMP_EREADER", "A_COMP_GAMPER", "A_COMP_PDA",
								"A_COMP_PERIPH", "A_COMP_PRINT", "A_COMP_PRTSUP", "A_COMP_SCANNER",
								"A_COMP_SOFTOP", "A_COMP_SOFTRC", "A_COMP_SOUNDANDVIDEOCARDS",
								"A_COMP_STORAGEMEDIA", "A_EGOODS_DIGITALAUDIOBOOKS",
								"A_EGOODS_DIGITALBOOK_RENTAL", "A_EGOODS_DIGITALBOOKS",
								"A_EGOODS_DIGITALGAMES", "A_EGOODS_DIGITALMUSIC", "A_EGOODS_DIGITALNEWS",
								"A_EGOODS_DIGITALNEWSSUBS", "A_EGOODS_DIGITALPERDCL",
								"A_EGOODS_DIGITALPERDCLSUBS", "A_EGOODS_MISC1", "A_EGOODS_ONLINEGAMINGSUBS",
								"A_EGOODS_SOFT", "A_EMERGENCY_SUPPLIES", "A_FOOD_BKTCDY50-90",
								"A_FOOD_BKTGN50-75", "A_FOOD_BKTGN76-90", "A_FOOD_CARBFLAVORWTR_NONSWEET",
								"A_FOOD_CARBSFTDK", "A_FOOD_CARBWTR", "A_FOOD_CNDY", "A_FOOD_CNDYFL",
								"A_FOOD_FLAVORWATER_NONSWEET", "A_FOOD_GEN", "A_FOOD_JUICE0-50",
								"A_FOOD_JUICE51-99", "A_FOOD_NCARBWTR", "A_FOOD_PETFOOD", "A_FOOD_WINE",
								"A_GEN_NOTAX", "A_GEN_RENTAL", "A_GEN_TAX", "A_HLTH_BABYSUPPLS",
								"A_HLTH_BANDKIT", "A_HLTH_BRAILLE", "A_HLTH_CONTACTSOLN",
								"A_HLTH_CONTRCEPV", "A_HLTH_COSMETIC", "A_HLTH_DIABSUPPLS",
								"A_HLTH_DIETSUPMT", "A_HLTH_DISPOSABLE_PACK", "A_HLTH_FACIALTISSUE",
								"A_HLTH_FAMPLANTEST", "A_HLTH_FEMHYG", "A_HLTH_FEMHYGSPRAY",
								"A_HLTH_HANDSANITIZER", "A_HLTH_HEARINGAID_BATT", "A_HLTH_INCONT",
								"A_HLTH_INCONTPADS", "A_HLTH_INCONTPADS_DISP", "A_HLTH_MEDHAIRLOSS",
								"A_HLTH_MISCDURABLEMED", "A_HLTH_MISCMOBILITY1", "A_HLTH_MISCMOBILITY2",
								"A_HLTH_MOBILITY", "A_HLTH_MONITOR", "A_HLTH_NASALSTRIP", "A_HLTH_OTCMED",
								"A_HLTH_PROSTHETIC", "A_HLTH_REMEDY", "A_HLTH_SPFCORALHYG",
								"A_HLTH_SPFCOTCMED", "A_HLTH_SUNSCRN", "A_HLTH_TENS", "A_HLTH_THRMTR",
								"A_HLTH_TISSUETOW", "A_HLTH_TOOTHPASTEFL", "A_HOUSEHOLD_LINEN",
								"A_HOUSEHOLD_PAPERPRODS", "A_HOUSEHOLD_SOLAR", "A_NEWS_104PLUS",
								"A_NEWS_12MTHLY", "A_NEWS_26BIWKLY", "A_NEWS_4QTLY", "A_NEWS_52WKLY",
								"A_PERDCL_104PLUS", "A_PERDCL_52W-4Q", "A_PERDCL_52WKLY", "A_PERDCL_ANNUAL",
								"A_PERDCL_SEMIANNL", "A_REST_COLDPREPARED", "A_REST_DELIVERY",
								"A_REST_HOTPREPARED", "A_SCHL_GRAPHCALC", "A_SCHL_INSTRUMENT",
								"A_SCHL_SUPPLS", "A_SERV_CLEAN", "A_SERV_INSTALL", "A_SPORT_ASUPPORT",
								"A_SPORT_ATHLSHOES", "A_SPORT_BIKEHLMT", "A_SPORT_MISCSPORTS1",
								"A_SPORT_PROTECTIVEGEAR", "A_SPORT_SKISUIT", "A_SPORT_WINTERSPORTBOOTS",
								"A_WARR_PARTSNSVCE"
							]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"merchant_release_date": {
			"title": "发售日期",
			"description": "卖家可以在该输入项内为商品提前设定可出售日期（在此日期之前该商品可见，但无法被购买）。",
			"examples": ["2004-05-01"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "发售日期",
						"description": "卖家可以在该输入项内为商品提前设定可出售日期（在此日期之前该商品可见，但无法被购买）。",
						"editable": true,
						"hidden": false,
						"examples": ["2004-05-01"],
						"type": "string",
						"oneOf": [{
							"type": "string",
							"format": "date"
						}, {
							"type": "string",
							"format": "date-time"
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"merchant_shipping_group": {
			"title": "配送模板",
			"description": "该商品使用的配送模板。卖家可以在“配送设置”页面创建和管理配送模板。卖家可以自定义模板名称，如“大件商品配送模板”、“促销配送模板”",
			"examples": ["大件商品配送模板"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "商船集团",
						"description": "出价的船舶配置组。卖方通过船舶设置 UI 创建和管理船舶配置组。",
						"editable": true,
						"hidden": false,
						"examples": ["重型产品、NCR 大型设备交付"],
						"type": "string",
						"enum": ["ba45ec04-c35b-474e-bb11-662b7b7deb7e", "legacy-template-id"],
						"enumNames": ["4.20 US Template", "Migrated Template"],
						"maxLength": 100
					}
				},
				"additionalProperties": false
			}
		},
		"max_order_quantity": {
			"title": "单笔订单最大数量",
			"description": "整数。",
			"examples": ["75"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "最大订单数量",
						"description": "整数。",
						"editable": true,
						"hidden": false,
						"examples": ["75"],
						"type": "integer",
						"minimum": 1
					}
				},
				"additionalProperties": false
			}
		},
		"gift_options": {
			"title": "礼品选项",
			"description": "提供礼品卡选项",
			"examples": ["是"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"can_be_messaged": {
						"title": "可以提供礼品信息",
						"description": "如果您可以打印礼品信息与项目，请在此注明。 如果留白，默认为“false”",
						"editable": true,
						"hidden": false,
						"examples": ["“TRUE”"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					},
					"can_be_wrapped": {
						"title": "表示礼品包装可用",
						"description": "如果你为一个项目做礼品包装，请在此注明。 如果留白，默认为“false”",
						"editable": true,
						"hidden": false,
						"examples": ["“TRUE”"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"main_offer_image_locator": {
			"title": "主商品图片定位器",
			"description": "提供图片的位置",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "主图片的地址",
						"description": "产品的交付主图所在的链接地址",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.offer.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_offer_image_locator_1": {
			"title": "其他商品图片定位器",
			"description": "提供图片的位置",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_offer_image_locator_2": {
			"title": "其他商品图片定位器",
			"description": "提供图片的位置",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_offer_image_locator_3": {
			"title": "其他商品图片定位器",
			"description": "提供图片的位置",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_offer_image_locator_4": {
			"title": "其他商品图片定位器",
			"description": "提供图片的位置",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_offer_image_locator_5": {
			"title": "其他商品图片定位器",
			"description": "提供图片的位置及来源",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"import_designation": {
			"title": "进口指定",
			"description": "为产品选择相应的进口设定",
			"examples": ["美国制造、美国制造或进口"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "进口设定",
						"description": "为产品选择相应的进口设定",
						"editable": true,
						"hidden": false,
						"examples": ["美国制造、美国制造或进口"],
						"type": "string",
						"maxLength": 2234,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["美国制造", "美国制造兼进口", "美国制造或进口", "进口"],
							"enumNames": ["美国制造", "美国制造兼进口", "美国制造或进口", "进口"]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"product_description": {
			"title": "描述",
			"description": "商品的文本描述。",
			"examples": [
				"当季必备单品，为您点亮温柔多情的双眸。本眼影组合可满足您的各种妆容需要，无论是自然娇柔风（米黄色和粉色）还是极致熟女风（褐色和棕色）。眼影粉质滑腻，既可单独使用，也可巧妙搭配，尽显层次感。"
			],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1000,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品描述",
						"description": "提供产品的文本说明。该信息将以段落形式显示在产品的详细信息页面上。包括产品的特点、产品系列详情和产品规格。请不要全部大写。",
						"editable": true,
						"hidden": false,
						"examples": [
							"这件华丽的墙壁装饰品是在高品质帆布上手绘的，随时可以悬挂。这款墙壁艺术品以蓝色和绿色的平静色调营造出鼓舞人心的自然景观，将为家中或办公室的任何空间带来宁静美好的感觉。由当地艺术家使用高品质环保材料手工制作。每件都是独一无二的定制作品。支持小型企业和当地艺术界。适合任何特别场合的有意义的礼物。"
						],
						"type": "string",
						"maxUtf8ByteLength": 10000
					}
				},
				"additionalProperties": false
			}
		},
		"bullet_point": {
			"title": "商品特性",
			"description": "简短的描述性文本，从某几个具体的角度，通过列举商品特性对商品进行说明。",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 10,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "要点",
						"description": "产品特定方面的简要描述文本以项目符号表示。这些符号显示在产品照片的正下方或旁边，在这些字段中放置具有吸引力的信息会很有帮助。不可使用全大写或缩写。因产品销售地域不同，请不要罗列出面料信息、保养说明或国家。",
						"editable": true,
						"hidden": false,
						"examples": ["带花朵的花卉设计"],
						"type": "string",
						"minLength": 0,
						"maxLength": 600
					}
				},
				"additionalProperties": false
			}
		},
		"generic_keyword": {
			"title": "搜索关键词",
			"description": "最能恰当描述商品的词语或短语。当买家在卓越亚马逊网站上搜索商品时，它能够帮助买家找到相应的商品。",
			"examples": ["维生素"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1000,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "搜索关键字",
						"description": "提供可能与客户搜索相关的任何术语没有重复、没有竞争对手的品牌名称或 ASIN。",
						"editable": true,
						"hidden": false,
						"examples": ["水上运动鞋；Derek Rose；电动；Wi-Fi；香蕉"],
						"type": "string",
						"minLength": 0,
						"maxLength": 500,
						"maxUtf8ByteLength": 2000
					}
				},
				"additionalProperties": false
			}
		},
		"lifestyle": {
			"title": "适用场合",
			"description": "说明商品是专为哪些生活方式而设计的。生活方式体现了正式程度。",
			"examples": ["运动，商务，商务休闲，多用途，正式"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "生活方式",
						"description": "说明商品是专为哪些生活方式而设计的。生活方式体现了正式程度。",
						"editable": true,
						"hidden": false,
						"examples": ["大众美, 高级美容"],
						"type": "string",
						"maxUtf8ByteLength": 2000,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["大众美", "高级美容"],
							"enumNames": ["大众美", "高级美容"]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"style": {
			"title": "风格名称",
			"description": "提供产品的样式。样式是指一个人或一群人的审美选择。它描述产品的独特视觉表现",
			"examples": ["装饰艺术"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "风格",
						"description": "提供产品的样式。样式是指一个人或一群人的审美选择。它描述产品的独特视觉表现",
						"editable": true,
						"hidden": false,
						"examples": ["装饰艺术"],
						"type": "string",
						"maxLength": 2200,
						"maxUtf8ByteLength": 2000,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["Vintage", "东方", "中国风", "休闲", "传统", "古典", "复古", "摩登", "日系风格", "法国风",
								"波希米亚", "现代", "经典的", "维多利亚"
							],
							"enumNames": ["Vintage", "东方", "中国风", "休闲", "传统", "古典", "复古", "摩登", "日系风格", "法国风",
								"波希米亚", "现代", "经典的", "维多利亚"
							]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"target_gender": {
			"title": "适用性别",
			"description": "请说明该商品所适用的性别。",
			"examples": ["男性 (male), 女性 (female), 通用 (unisex)"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "目标性别",
						"description": "提供产的目标性别",
						"editable": true,
						"hidden": false,
						"examples": ["女性"],
						"type": "string",
						"enum": ["female", "male", "unisex"],
						"enumNames": ["女性", "男性", "通用"]
					}
				},
				"additionalProperties": false
			}
		},
		"age_range_description": {
			"title": "年龄范围说明",
			"description": "指定商品适用的年龄范围说明",
			"examples": ["婴儿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "年齡範圍說明",
						"description": "指定商品适用的年龄范围说明",
						"editable": true,
						"hidden": false,
						"examples": ["婴儿"],
						"type": "string",
						"maxLength": 1998,
						"maxUtf8ByteLength": 2000,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["儿童", "婴儿", "幼儿", "成人", "青少年"],
							"enumNames": ["儿童", "婴儿", "幼儿", "成人", "青少年"]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"material": {
			"title": "材质类型",
			"description": "请简要说明商品的制造原材料。",
			"examples": ["玻璃"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 14,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "材質",
						"description": "指定用来制造产品的主要材料",
						"editable": true,
						"hidden": false,
						"examples": ["塑料"],
						"type": "string",
						"maxUtf8ByteLength": 2000,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["不锈钢", "丙烯腈丁二烯苯乙烯", "丙烯酸", "乳胶", "亚克力混纺", "亚麻", "人造皮草", "人造纤维", "人造革",
								"仿麂皮", "兔毛", "合金钢", "塑料", "尼龙", "尼龙混纺", "布", "抓毛绒", "方晶皓石", "有机硅", "木",
								"树脂", "棉", "棉混纺", "橡胶", "毛皮", "氨纶", "氨纶混纺", "氯丁橡胶", "水晶", "泡沫", "涤纶混纺",
								"牛仔布", "狐狸毛", "皮革", "真丝", "粘纤", "纸", "织物", "网布", "网格", "羊毛", "羊毛混纺", "羊绒",
								"聚丙烯 (PP)", "聚氨酯", "聚碳酸酯", "聚酯棉布", "聚酯纤维", "莱茵石", "貂皮", "贝壳", "超细纤维",
								"醋酸乙烯酯乙烯", "醋酸纤维素", "金属", "铁", "铜", "锌", "雪纺绸", "麂皮", "黄铜"
							],
							"enumNames": ["不锈钢", "丙烯腈丁二烯苯乙烯", "丙烯酸", "乳胶", "亚克力混纺", "亚麻", "人造皮草", "人造纤维", "人造革",
								"仿麂皮", "兔毛", "合金钢", "塑料", "尼龙", "尼龙混纺", "布", "抓毛绒", "方晶皓石", "有机硅", "木",
								"树脂", "棉", "棉混纺", "橡胶", "毛皮", "氨纶", "氨纶混纺", "氯丁橡胶", "水晶", "泡沫", "涤纶混纺",
								"牛仔布", "狐狸毛", "皮革", "真丝", "粘纤", "纸", "织物", "网布", "网格", "羊毛", "羊毛混纺", "羊绒",
								"聚丙烯 (PP)", "聚氨酯", "聚碳酸酯", "聚酯棉布", "聚酯纤维", "莱茵石", "貂皮", "贝壳", "超细纤维",
								"醋酸乙烯酯乙烯", "醋酸纤维素", "金属", "铁", "铜", "锌", "雪纺绸", "麂皮", "黄铜"
							]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"fabric_type": {
			"title": "布料",
			"description": "列出所有以 “,” 划分的面料。用 % 成分表示。总是添加 “粘胶纤维” 或 “人造丝” 而不是 “竹制”，并用 “ 人造蛋白质纤维” 代替大豆",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1000,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "面料类型",
						"description": "列出所有以 “,” 划分的面料。用 % 成分表示。总是添加 “粘胶纤维” 或 “人造丝” 而不是 “竹制”，并用 “ 人造蛋白质纤维” 代替大豆",
						"editable": true,
						"hidden": false,
						"examples": ["60% 棉, 40% 涤纶"],
						"type": "string",
						"maxLength": 2311
					}
				},
				"additionalProperties": false
			}
		},
		"number_of_items": {
			"title": "包装内数量",
			"description": "单个商品的总数量，此类商品未经过独立包装，不可用于散售。例如，一袋 100 个棉球的包装内数量为 100。一盒装有 10 个独立包装的小袋，每个小袋中有 100 个棉球，则独立包装数量为 10，包装内数量 为 1000。",
			"examples": [
				"包含的单个商品的总数量，此类商品未经过独立包装，不可单独销售。例如，一袋 100个的棉球，包装内数量为100。一盒里有10个独立的包装袋，每个包装袋中有100个棉球，则包装内数量为1000。"
			],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品数量",
						"description": "向客户提供单位销售中相同产品的总数",
						"editable": true,
						"hidden": false,
						"examples": ["5"],
						"type": "integer",
						"minimum": 0,
						"maximum": 999999999999,
						"anyOf": [{
							"type": "integer"
						}, {
							"type": "integer",
							"enum": [1, 12, 2, 3, 4, 5, 6],
							"enumNames": ["1", "12", "2", "3", "4", "5", "6"]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"item_package_quantity": {
			"title": "包装内物品数量",
			"description": "描述该商品中所包含的独立物品总数。例如，您出售的商品为一箱方便面，共包含12包方便面，则该值应为12。",
			"examples": ["3"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "成套产品数量",
						"description": "提供单一产品的成套销售数量。ASIN 销售 5 盒回形针，每盒包含 100 个回形针，则此产品成套数量 =“5”",
						"editable": true,
						"hidden": false,
						"examples": ["1"],
						"type": "integer",
						"minimum": 1
					}
				},
				"additionalProperties": false
			}
		},
		"special_size_type": {
			"title": "特殊尺寸",
			"description": "指明商品的特殊尺寸类型",
			"examples": ["偏胖、娇小"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "特殊尺寸",
						"description": "指明商品的特殊尺寸类型",
						"editable": true,
						"hidden": false,
						"examples": ["女款小号"],
						"type": "string",
						"maxLength": 2200
					}
				},
				"additionalProperties": false
			}
		},
		"color": {
			"title": "颜色",
			"description": "提供产品颜色",
			"examples": ["蔓越橘色"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag"],
				"properties": {
					"standardized_values": {
						"title": "色彩地图",
						"description": "选择产品最主要的颜色。",
						"editable": true,
						"hidden": false,
						"examples": ["红色"],
						"type": "array",
						"minItems": 1,
						"maxItems": 1,
						"items": {
							"type": "string",
							"anyOf": [{
								"type": "string"
							}, {
								"type": "string",
								"enum": ["多色", "棕色", "橘色", "灰白色", "灰色", "白色", "米黄色", "粉紅色", "紅色", "紫色", "綠色",
									"绿松石色", "藍色", "透明", "金屬光澤", "金色", "銀色", "青铜色", "黃色", "黑色"
								],
								"enumNames": ["多色", "棕色", "橘色", "灰白色", "灰色", "白色", "米黄色", "粉紅色", "紅色", "紫色",
									"綠色", "绿松石色", "藍色", "透明", "金屬光澤", "金色", "銀色", "青铜色", "黃色", "黑色"
								]
							}]
						}
					},
					"value": {
						"title": "颜色",
						"description": "提供产品颜色",
						"editable": true,
						"hidden": false,
						"examples": ["红色"],
						"type": "string",
						"minLength": 0,
						"maxLength": 1000
					},
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"size": {
			"title": "尺寸",
			"description": "如果您的商品按照尺寸区分，请使用该输入项说明商品的尺寸。",
			"examples": ["小，16 盎司"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "尺码",
						"description": "提供产品尺码",
						"editable": false,
						"hidden": false,
						"examples": ["特大号"],
						"type": "string",
						"maxLength": 50,
						"maxUtf8ByteLength": 2000
					}
				},
				"additionalProperties": false
			}
		},
		"metal_type": {
			"title": "金属种类",
			"description": "提供商品的金属类型。",
			"examples": ["青铜、镀铂金黄铜"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 5,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "金属类型",
						"description": "提供商品的金属类型。",
						"editable": true,
						"hidden": false,
						"examples": ["青铜、镀铂金黄铜"],
						"type": "string",
						"maxLength": 540
					}
				},
				"additionalProperties": false
			}
		},
		"occasion_type": {
			"title": "适于场合",
			"description": "说明商品是专为哪些场合而设计的。场合类型包括节假日和重要人生事件。",
			"examples": ["周年，生日，婚礼"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 44,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "場合",
						"description": "说明商品是专为哪些场合而设计的。场合类型包括节假日和重要人生事件。",
						"editable": true,
						"hidden": false,
						"examples": ["周年，生日，婚礼"],
						"type": "string",
						"maxLength": 1381,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["单身女郎派对", "周年纪念日", "圣帕特里克节", "圣餐", "妇女节", "婚礼", "婴儿送礼会", "情人节", "新娘婚前派对",
								"新年", "母亲节", "毕业", "毕业舞会", "洗礼", "父亲节", "甜蜜十六岁", "生日会", "节日", "访校日", "退休",
								"（十五岁女孩）成人礼"
							],
							"enumNames": ["单身女郎派对", "周年纪念日", "圣帕特里克节", "圣餐", "妇女节", "婚礼", "婴儿送礼会", "情人节",
								"新娘婚前派对", "新年", "母亲节", "毕业", "毕业舞会", "洗礼", "父亲节", "甜蜜十六岁", "生日会", "节日",
								"访校日", "退休", "（十五岁女孩）成人礼"
							]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"part_number": {
			"title": "制造商零件编号",
			"description": "如适用，请提交商品的制造商零件编号。大多数商品的制造商零件编号与其型号相同，但也有一些制造商会区分零件编号和型号。",
			"examples": ["SB-122"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "零件编号",
						"description": "提供零件编号。在许多产品中，此编号与型号相同，但有些制造商会将零件编号与型号区分表示",
						"editable": false,
						"hidden": false,
						"examples": ["RIV001"],
						"type": "string",
						"maxLength": 40
					}
				},
				"additionalProperties": false
			}
		},
		"item_shape": {
			"title": "商品形状",
			"description": "指定商品形状",
			"examples": ["圆形"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "商品形状",
						"description": "指定商品形状",
						"editable": true,
						"hidden": false,
						"examples": ["圆形"],
						"type": "string",
						"maxUtf8ByteLength": 2000
					}
				},
				"additionalProperties": false
			}
		},
		"ring": {
			"title": "戒指",
			"description": "该属性表示产品的环",
			"examples": ["均码"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"size": {
						"title": "圈口尺寸",
						"description": "提供戒指尺寸",
						"examples": ["4.25、7.5、8.75"],
						"type": "array",
						"minItems": 1,
						"minUniqueItems": 1,
						"maxUniqueItems": 1,
						"selectors": ["language_tag"],
						"items": {
							"type": "object",
							"required": ["language_tag", "value"],
							"properties": {
								"language_tag": {
									"$ref": "#/$defs/language_tag"
								},
								"value": {
									"title": "戒指尺寸",
									"description": "提供戒指尺寸",
									"editable": true,
									"hidden": false,
									"examples": ["4.25、7.5、8.75"],
									"type": "string",
									"maxUtf8ByteLength": 5000
								}
							},
							"additionalProperties": false
						}
					}
				},
				"additionalProperties": false
			}
		},
		"edition": {
			"title": "版本",
			"description": "提供商品的版本或版次",
			"examples": ["教师版次、未删减版本"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "版次",
						"description": "提供商品的版本或版次",
						"editable": true,
						"hidden": false,
						"examples": ["教师版次、未删减版本"],
						"type": "string",
						"maxLength": 4582
					}
				},
				"additionalProperties": false
			}
		},
		"product_benefit": {
			"title": "产品优点",
			"description": "提供产品的优点/效果（最多 115 个字符）。避免使用特殊字符、大写字母和项目符号。",
			"examples": ["“轻盈卷发，健康飘逸，柔软丝滑”、“含有六种活性成分和生发因子的护发素。”"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 6,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品优点",
						"description": "提供产品的优点/效果（最多 115 个字符）。避免使用特殊字符、大写字母和项目符号。",
						"editable": true,
						"hidden": false,
						"examples": ["“轻盈卷发，健康飘逸，柔软丝滑”、“含有六种活性成分和生发因子的护发素。”"],
						"type": "string",
						"maxLength": 411,
						"maxUtf8ByteLength": 375
					}
				},
				"additionalProperties": false
			}
		},
		"lens": {
			"title": "镜片",
			"description": "该属性表示产品的镜头",
			"examples": ["耐化学涂层"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"color": {
						"title": "镜片颜色",
						"description": "提供镜头颜色",
						"examples": ["深红、天蓝/淡灰、蓝色、翠绿"],
						"type": "array",
						"minItems": 1,
						"minUniqueItems": 1,
						"maxUniqueItems": 1,
						"selectors": ["language_tag"],
						"items": {
							"type": "object",
							"required": ["language_tag", "value"],
							"properties": {
								"language_tag": {
									"$ref": "#/$defs/language_tag"
								},
								"value": {
									"title": "镜片颜色",
									"description": "提供镜头颜色",
									"editable": true,
									"hidden": false,
									"examples": ["深红、天蓝/淡灰、蓝色、翠绿"],
									"type": "string",
									"maxUtf8ByteLength": 2000
								}
							},
							"additionalProperties": false
						}
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"item_display_dimensions": {
			"title": "商品显示尺寸",
			"description": "提供产品的尺寸（无包装，无需完全组装）",
			"examples": ["5.7英寸 x 3.5英寸 x 1.8英寸"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"length": {
						"title": "商品显示长度",
						"description": "给出产品长度（无包装，无需完全组装）",
						"examples": ["5.7"],
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "商品显示长度单位",
								"description": "选择相应的单位",
								"editable": true,
								"hidden": false,
								"examples": ["毫升"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "商品显示长度",
								"description": "给出产品长度的数值分量（无包装，无需完全组装）",
								"editable": true,
								"hidden": false,
								"examples": ["5.7"],
								"type": "number",
								"maxLength": 5000
							}
						},
						"additionalProperties": false
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"flavor": {
			"title": "味道",
			"description": "使用该输入项说明您的商品的味道。",
			"examples": ["薄荷"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "口味",
						"description": "指定产品的口味",
						"editable": true,
						"hidden": false,
						"examples": ["双倍浓郁巧克力、樱桃、巧克力、香草"],
						"type": "string",
						"maxLength": 2200
					}
				},
				"additionalProperties": false
			}
		},
		"short_product_description": {
			"title": "产品短说明",
			"description": "产品的有限长度说明。最多 229 个字符（包括空格）。",
			"examples": ["双重作用的去角质剂，含果酸和温和的机械去角质成分，可清洁毛孔并去除死皮细胞。"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品短说明",
						"description": "产品的有限长度说明。最多 229 个字符（包括空格）。",
						"editable": true,
						"hidden": false,
						"examples": ["双重作用的去角质剂，含果酸和温和的机械去角质成分，可清洁毛孔并去除死皮细胞。"],
						"type": "string",
						"minLength": 0,
						"maxLength": 6594
					}
				},
				"additionalProperties": false
			}
		},
		"voltage": {
			"title": "伏特",
			"description": "提供产品的电压",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"value": {
						"title": "电压",
						"description": "以数值提供电压值",
						"editable": true,
						"hidden": false,
						"examples": ["28"],
						"type": "number",
						"minimum": 0
					},
					"unit": {
						"title": "电压单位",
						"description": "选择 Voltage 的度量单位。如果给出 Voltage 的值，还必须输入相应的单位。",
						"editable": true,
						"hidden": false,
						"examples": ["伏特"],
						"type": "string",
						"enum": ["volts", "volts_of_alternating_current", "volts_of_direct_current", "kilovolts",
							"microvolts", "millivolts", "nanovolts"
						],
						"enumNames": ["伏特", "伏特 (AC)", "伏特 (DC)", "千伏特", "微伏", "毫伏", "纳伏"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"volume_capacity_name": {
			"title": "容量",
			"description": "提供商品的容量（用数值表示）",
			"examples": ["7、16"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"unit": {
						"title": "容量的单位",
						"description": "选择相应的单位",
						"editable": true,
						"hidden": false,
						"examples": ["夸脱、升、加仑"],
						"type": "string",
						"enum": ["thirty_seconds_inches", "amperes_per_watts", "accelerated_reader", "adult_mx",
							"adult_uk", "adult_us", "afghan_afghani", "albanian_lek", "algerian_dinar",
							"angolan_kwanza", "angstrom", "arc_minute", "argentine_peso", "armenian_dram",
							"aruban_florin", "athens_reading_level", "australian_dollars", "austrian_schillings",
							"azerbaijani_manat", "baby_boys_us", "baby_girls_us", "bahamian_dollar",
							"bahraini_dinar", "bangladeshi_taka", "bante", "food_bars", "barbados_dollar", "barcol",
							"belarusian_ruble", "belgian_francs", "belize_dollar", "bermudian_dollar",
							"bhutanese_ngultrum", "big_boys_us", "big_child_us", "big_girls_us", "bits",
							"bits_per_second", "boliviano", "botswana_pula", "boys_us", "brazilian_real",
							"brinnell", "brinell", "brunei_dollar", "btu_per_hour_per_foot_per_fahrenheit",
							"bulgarian_lev", "burundian_franc", "bytes_per_second", "canadian_dollars", "calls",
							"cambodian_riel", "candela", "cape_verde_escudo", "cayman_islands_dollar",
							"center_beam_candle_power", "cubic_centimeters_per_minute",
							"cubic_centimeters_per_second", "centiliters", "centimeters_mercury",
							"centimeters_water", "centimeters_per_second", "central_african_cfa_franc", "cfp_franc",
							"cubic_feet_per_hour", "characters_per_inch", "child_au", "child_mx", "child_uk",
							"chilean_peso", "chinese_yuan", "cubic_inches_per_minute", "cubic_meters_per_hour",
							"co", "colombian_peso", "comoro_franc", "congolese_franc", "consulting",
							"convertible_mark", "costa_rican_colon", "candle_power", "centipoise", "croatian_kuna",
							"centistokes", "cttw", "cuban_convertible_peso", "cuban_peso",
							"cubic_feet_per_minute_per_watt", "cubic_inches", "cycles_per_gallon",
							"cycles_per_liter", "czech_koruna", "daltons", "danish_krone", "decibels",
							"decibel_volt_per_pascal", "decibel_volt_per_micro_bar", "deciliters",
							"degrees_balling", "degrees_baume", "degrees_brix", "degrees_ochsle",
							"degrees_per_second_squared", "german_mark", "deputy", "din", "diopters",
							"djiboutian_franc", "dominican_peso", "drams", "drp_score", "dwt", "dyne_centimeters",
							"east_caribbean_dollar", "egyptian_pound", "emails", "eritrean_nakfa", "ethiopian_birr",
							"euro", "exafarad", "f_stop", "falkland_islands_pound", "feet_per_second_squared",
							"femtofarad", "femtoseconds", "fiji_dollar", "finnish_markka", "foot_pounds",
							"feet_per_minute", "frames_per_second", "fr_school_grade", "french_francs",
							"foot_ounces", "gravity", "gallons_per_day", "grams_per_liter", "gallons_per_flush",
							"gallons_per_hour", "gallons_per_minute", "gambian_dalasi", "gauge", "GB",
							"gigabits_per_second", "uk_pounds", "gigabytes_per_hour", "gigabytes_per_second",
							"georgian_lari", "ghanaian_cedi", "GHz", "gibraltar_pound", "giga_samples_per_second",
							"gigaohms", "gigapascals", "girls_us", "gram_meters", "grams_per_100_kilometers",
							"grams_per_cubic_centimeter", "grams_per_kilometer", "grams_per_milliliter",
							"grams_per_minute", "grams_per_square_meter", "greek_drachma", "guatemalan_quetzal",
							"guinean_franc", "guyanese_dollar", "henry", "haitian_gourde", "honduran_lempira",
							"hong_kong_dollar", "hours_per_gallon", "hours_per_liter", "horsepower", "hectopascal",
							"hsl", "hsv", "hungarian_forint", "hertz", "icelandic_krona", "images_per_minute",
							"imperial_gallons", "inches_mercury", "inches_per_inches_degrees_fahrenheit",
							"inch_ounces", "inch_pounds", "inches_water", "inches_per_second",
							"inches_per_second_squared", "inches_squared", "indonesian_rupiah", "infant_uk",
							"infant_us", "indian_rupee", "iranian_rial", "iraqi_dinar", "irla_score", "iso",
							"israeli_new_shekel", "italian_lira", "jamaican_dollar", "jordanian_dinar",
							"japanese_yen", "juniors_us", "kazakhstani_tenge", "KB", "Kbaud", "kilobits_per_second",
							"kilobytes_per_second", "kelvin", "kenyan_shilling", "kilohenry", "KHz",
							"kilo_samples_per_second", "kiloamps", "kilodaltons", "kilogram_centimeters",
							"kilogram_meters", "kilograms_per_cubic_meter", "kilograms_per_liter",
							"kilograms_per_millimeter", "kilograms_per_square_centimeter", "kilonewton_meters",
							"kilonewtons", "knoop", "kilopascal", "kips_per_square_inch", "kuwaiti_dinar",
							"kyrgyzstani_som", "liters_per_day", "liters_per_second", "liters_per_year", "lao_kip",
							"latvian_lats", "lebanese_pound", "lesotho_loti", "levels", "lexile", "lexile_code",
							"lexile_number", "liberian_dollar", "libyan_dinar", "lines", "links_per_foot",
							"links_per_inch", "liters_per_100_kilometers", "liters_per_flush", "lithuanian_litas",
							"little_boys_us", "little_child_us", "little_girls_us", "lumen", "load",
							"liters_per_hour", "liters_per_minute", "lux", "luxembourg_franc",
							"meters_per_meters_kelvin", "macanese_pataca", "macedonian_denar", "malagasy_ariary",
							"malawian_kwacha", "malaysian_ringgit", "maldivian_rufiyaa", "mauritanian_ouguiya",
							"mauritian_rupee", "MB", "megabits", "megabits_per_second", "megabytes_per_hour",
							"megabytes_per_second", "mega_samples_per_second", "megaohms", "megapascals", "mens_au",
							"mens_uk", "mens_us", "mesh_count_per_square_inch", "messages", "meters_per_second",
							"meters_per_second_squared", "metric_tons_per_hectare", "mexican_peso", "millihenry",
							"MHz", "microamps", "microcandela", "microfarad", "micrograms", "microhenry",
							"microhertz", "microliters", "microliters_per_minute", "microliters_per_second",
							"micron", "micronewton_meters", "microohms", "microradian", "microwatts",
							"milliamp_hours", "milliampere_hour", "milliampere_second", "milliamps", "millicandela",
							"millihertz", "milliliters_per_second", "millimeters_water", "millimeters_per_second",
							"millimeters_water_per_square_centimeter", "millinewton_meters", "milliradian",
							"milliwatts_per_centimeters_squared", "mils", "minutes_per_liter",
							"milliliters_per_minute", "millimeters_mercury", "mohs", "moldovan_leu",
							"mongolian_togrog", "moroccan_dirham", "mozambican_metical", "megapixels",
							"mean_spherical_candle_power", "millivolts_per_gravity", "millivolts_per_pascals",
							"millivolts_per_pounds_per_square_inch", "myanma_kyat", "namibian_dollar", "nanoamps",
							"nanofarad", "nanoliters", "nanoliters_per_minute", "nanoliters_per_second", "nanoohms",
							"nanovolts", "nanowatts", "nepalese_rupee", "netherlands_antillean_guilder",
							"new_taiwan_dollar", "new_zealand_dollar", "newton_centimeters", "newton_meters",
							"newton_millimeters", "newtons_per_square_millimeter", "nanohenry",
							"nicaraguan_cordoba", "nigerian_naira", "north_korean_won", "norwegian_krone",
							"ohms_per_centimeter", "ohms_per_inch", "ohms_per_meter", "omani_rial",
							"openings_per_square_cm", "openings_per_square_inch", "operations",
							"ounces_per_cubic_inch", "ounces_per_square_inch", "poise", "pascal_seconds", "pascal",
							"pages_per_month", "pages_per_second", "pages_per_sheet", "pakistani_rupee",
							"panamanian_balboa", "papua_new_guinean_kina", "paraguayan_guarani",
							"peruvian_nuevo_sol", "petafarad", "philippine_peso", "photos", "picoamps", "picofarad",
							"picoliters", "picoliters_per_minute", "picoliters_per_second", "picoohms",
							"picoseconds", "picowatts", "pictures", "pills", "pitch", "pods", "polish_zloty",
							"portions", "portuguese_escudos", "pound_per_square_foot", "pounds",
							"pounds_per_cubic_foot", "pounds_per_cubic_inch", "pounds_per_cubic_yard",
							"pounds_per_inch", "pounds_per_square_yard", "parts_per_billion", "parts_per_hundred",
							"parts_per_million", "parts_per_quadrillion", "parts_per_thousand", "primary", "pulses",
							"qatari_riyal", "r_value", "r_value_metric", "radians_per_second_squared",
							"revolutions_per_second_squared", "revolutions", "rockwell_15N", "rockwell_15T",
							"rockwell_30N", "rockwell_30T", "rockwell_45N", "rockwell_45T", "rockwell_a",
							"rockwell_b", "rockwell_c", "rockwell_d", "rockwell_E", "rockwell_f", "rockwell_g",
							"rockwell_h", "rockwell_k", "rockwell_l", "rockwell_M", "rockwell_p", "rockwell_R",
							"rockwell_s", "rockwell_v", "romanian_leu", "rows", "russian_rouble", "russian_ruble",
							"rwandan_franc", "stokes", "saint_helena_pound", "samoan_tala", "samples_per_second",
							"saudi_riyal", "scoops", "serbian_dinar", "seychelles_rupee", "shore_a", "shore_b",
							"shore_c", "shore_d", "shore_do", "shore_e", "shore_m", "shore_o", "shore_oo",
							"shore_ooo", "shore_ooo_s", "shore_r", "sierra_leonean_leone", "singapore_dollar",
							"solomon_islands_dollar", "somali_shilling", "sones", "south_african_rand",
							"south_korean_won", "south_sudanese_pound", "spanish_pesetas", "sun_protection_factor",
							"square_centimeters", "square_feet", "square_meters", "sri_lankan_rupee", "stops",
							"sudanese_pound", "surinamese_dollar", "swazi_lilangeni", "swedish_krona",
							"swiss_francs", "syrian_pound", "sao_tome_and_principe_dobra", "tablespoons",
							"tajikistani_somoni", "tanzanian_shilling", "TB", "terabytes_per_second", "teaspoons",
							"teen_boys_us", "teen_girls_us", "teen_us", "teeth", "teeth_per_inch",
							"ten_thousandths_inches", "teraohms", "tgw", "thai_baht", "thousandths_inches",
							"threads_per_centimeter", "threads_per_inch", "terahertz", "toddler_uk", "toddler_us",
							"tog", "tongan_paanga", "tons", "tons_per_acre", "torr", "trinidad_and_tobago_dollar",
							"tunisian_dinar", "turkish_lira", "turkmenistani_manat", "turns",
							"turns_per_centimeter", "turns_per_inch", "ugandan_shilling", "ukrainian_hryvnia",
							"unidad_de_valor_real", "unisex_br", "unisex_eu", "unisex_jp",
							"united_arab_emirates_dirham", "units", "unknown_modifier", "uruguayan_peso",
							"uzbekistan_som", "volts_per_gravity", "vanuatu_vatu", "venezuelan_bolivar", "vickers",
							"vietnamese_dong", "volt_amperes", "volts_of_alternating_current",
							"volts_of_direct_current", "watts_per_kilogram", "watts_per_meter_per_celsius",
							"watts_per_meter_per_kelvin", "west_african_cfa_franc", "wir_euro", "wir_franc",
							"womens_au", "womens_uk", "womens_us", "words", "multiplier_x", "yemeni_rial",
							"yoctofarad", "yottafarad", "youth_uk", "youth_us", "zambian_kwacha", "zeptofarad",
							"zettafarad", "zimbabwe_dollar", "terafarad", "volts", "percent_by_volume", "pixels",
							"parts_per_trillion", "grams", "carats", "minutes", "decimeters", "minute", "gallons",
							"assistant", "nanoseconds", "gigafarad", "decifarad", "decafarad", "kilovolts",
							"kilopixel", "kilograms", "milliohms", "kilocalories", "kiloohms", "kilofarad",
							"kilojoules", "kilowatts", "kilowatt_hours", "kilowatt_hours_per_year", "kilometer",
							"kilometers", "kilometers_per_hour", "liters", "degrees_fahrenheit", "unit_of_alcohol",
							"calories", "centiliters_per_second", "centimeters", "centimeter_kilograms", "weeks",
							"pints", "cycles", "quarts", "bytes", "quarters", "amps", "amp_hours", "ampules",
							"hour", "hours", "layers", "bars", "centimeters_squared",
							"centimeters_per_second_squared", "square_inches", "years", "degrees",
							"degrees_per_second", "radians", "microvolts", "microseconds", "micrometer",
							"radians_per_second", "degrees_celsius", "place_settings", "days", "months", "cups",
							"atmosphere", "ohm", "kilowatt_hours_per_100_cycles", "kilowatt_hours_per_1000_hours",
							"rpm", "pages_per_minute", "percent_daily_value_fda", "revolutions_per_week",
							"revolutions_per_second", "pixels_per_inch", "dots_per_inch", "lines_per_inch",
							"millivolts", "milligrams", "milliliters", "millibars", "millifarad", "milliwatts",
							"milliseconds", "millimeters", "farad", "fluid_ounces", "dots", "joules", "newtons",
							"watts", "watt_hours", "mt_s", "megafarad", "centifarad", "hundredths_pounds",
							"hundredths_inches", "percentage", "hectofarad", "picometer", "ounces", "yards",
							"pounds_per_square_inch", "seconds", "cubic_centimeters", "cubic_yards", "cubic_meters",
							"cubic_meters_per_minute", "cubic_meters_per_second", "cubic_feet",
							"cubic_feet_per_minute", "cubic_feet_per_second", "chapters", "us_school_grade",
							"meter", "meters", "nanometer", "us_dollars", "capsules", "btus", "inches", "feet",
							"feet_per_second", "mile", "miles", "miles_per_hour", "ph", "arc_sec", "rotations",
							"revolutions_per_month", "revolutions_per_hour", "percent_by_weight", "attofarad",
							"pages"
						],
						"enumNames": ["32nds", "A/W", "Accelerated Reader", "Adult MEX", "Adult UK", "Adult US",
							"Afghan afghani", "Albanian lek", "Algerian dinar", "Angolan kwanza", "Angstrom",
							"arcmin", "Argentine peso", "Armenian dram", "Aruban florin", "Athens Reading Level",
							"Australian Dollars", "Austrian Schillings", "Azerbaijani manat", "Baby Boys US",
							"Baby Girls US", "Bahamian dollar", "Bahraini dinar", "Bangladeshi taka", "Bante",
							"bar(s)", "Barbados dollar", "Barcol", "Belarusian ruble", "Belgian Francs",
							"Belize dollar", "Bermudian dollar", "Bhutanese ngultrum", "Big Boys US",
							"Big Child US", "Big Girls US", "Bits", "Bits每秒", "Boliviano", "Botswana pula",
							"Boys US", "Brazilian Real", "Brinell", "Brinell", "Brunei dollar",
							"BTU per Hour per Foot per Degree Fahrenheit", "Bulgarian lev", "Burundian franc",
							"Bytes每秒", "CAD", "Calls", "Cambodian riel", "Candela", "Cape Verde escudo",
							"Cayman Islands dollar", "cbcp", "cc/min", "cc/sec", "Centiliters",
							"Centimeters of Mercury", "Centimeters of Water", "Centimeters per Second",
							"Central African CFA franc", "CFP franc", "CFPH", "Characters Per Second", "Child AU",
							"Child MEX", "Child UK", "Chilean peso", "Chinese Yuan", "CIPM", "CMPH", "Co.",
							"Colombian peso", "Comoro franc", "Congolese franc", "Consulting", "convertible mark",
							"Costa Rican colon", "cp", "cp", "Croatian kuna", "CS", "CTTW",
							"Cuban convertible peso", "Cuban peso", "Cubic Feet Per Minute Per Watt",
							"Cubic Inches", "Cycles per Gallon", "Cycles Per Liter", "Czech koruna", "Daltons",
							"Danish Krone", "dB", "dBV/Pascal", "dBV/uBar", "Deciliters", "Degrees Balling",
							"Degrees Baume", "Degrees Brix", "Degrees Oechsle", "Degrees per Second Squared", "DEM",
							"Deputy", "DIN", "Diopters", "Djiboutian franc", "Dominican peso", "Drams", "DRP Score",
							"DWT", "Dyne-cm", "East Caribbean dollar", "Egyptian pound", "Emails", "Eritrean nakfa",
							"Ethiopian birr", "EUR", "Exafarad", "f", "Falkland Islands pound",
							"Feet per Second Squared", "Femtofarad", "Femtoseconds", "Fiji dollar",
							"Finnish Markka", "Foot Pounds", "FPM", "fps", "FR School Grade", "FRF", "ft-oz", "g",
							"G/day", "g/L", "Gallons per Flush", "Gallons per Hour", "Gallons per Minute",
							"Gambian dalasi", "Gauge", "GB", "Gbits每秒", "GBP", "Gbytes每小时", "Gbytes每秒",
							"Georgian lari", "Ghanaian cedi", "GHz", "Gibraltar pound", "Giga Samples per Second",
							"Gigaohms", "gigapascals", "Girls US", "Gram Meters", "Grams per 100 kilometers",
							"Grams per Cubic Centimeter", "Grams per Kilometer", "Grams per Milliliter",
							"Grams Per Minute", "Grams per Square Meter", "Greek Drachma", "Guatemalan quetzal",
							"Guinean franc", "Guyanese dollar", "H", "Haitian gourde", "Honduran lempira",
							"Hong Kong dollar", "Hours per Gallon", "Hours per Liter", "hp", "hPa", "HSL", "HSV",
							"Hungarian forint", "Hz", "Icelandic króna", "Images per Minute", "Imperial Gallons",
							"in Hg", "in/in Degrees Fahrenheit", "Inch Ounces", "Inch Pounds", "Inches of Water",
							"Inches per Second", "Inches per Second Squared", "Inches Squared", "Indonesian Rupiah",
							"Infant UK", "Infant US", "INR", "Iranian rial", "Iraqi dinar", "IRLA Score", "ISO",
							"Israeli new shekel", "Italian Lira", "Jamaican dollar", "Jordanian dinar", "JPY",
							"Juniors US", "Kazakhstani tenge", "KB", "KBaud", "Kbps", "Kbps", "Kelvin",
							"Kenyan shilling", "KH", "KHz", "Kilo Samples per Second", "Kiloamps", "Kilodaltons",
							"Kilogram Centimeters", "Kilogram Meters", "Kilograms per Cubic Meter",
							"Kilograms per Liter", "Kilograms per Millimeter", "Kilograms Per Square Centimeter",
							"Kilonewton Meters", "Kilonewtons", "Knoop", "kPa", "KSI", "Kuwaiti dinar",
							"Kyrgyzstani som", "L/day", "L/sec", "L/year", "Lao kip", "Latvian lats",
							"Lebanese pound", "Lesotho loti", "Levels", "Lexile", "Lexile Code", "Lexile Number",
							"Liberian dollar", "Libyan dinar", "Lines", "Links Per Foot", "Links Per Inch",
							"Liters per 100 Kilometers", "Liters Per Flush", "Lithuanian litas", "Little Boys US",
							"Little Child US", "Little Girls US", "lm", "load", "LPH", "LPM", "Lux",
							"Luxembourg Franc", "m/m Kelvin", "Macanese pataca", "Macedonian denar",
							"Malagasy ariary", "Malawian kwacha", "Malaysian ringgit", "Maldivian rufiyaa",
							"Mauritanian ouguiya", "Mauritian rupee", "MB", "Mbit", "Mbits每秒", "Mbytes每小时",
							"Mbytes每秒", "Mega Samples per Second", "Megaohms", "megapascals", "Mens AU", "Mens UK",
							"Mens US", "Mesh Count Per Square Inch", "Messages", "Meters per Second",
							"Meters per Second Squared", "Metric Tons per Hectare", "Mexican Peso", "mH", "MHz",
							"Microamps", "microCandela", "Microfarad", "Micrograms", "microH", "Microhertz",
							"Microliters", "Microliters per Minute", "Microliters per Second", "Micron",
							"Micronewton Meters", "Microohms", "microrad", "Microwatts", "Milliamp Hours",
							"Milliampere Hour (mAh)", "Milliampere Second (mAs)", "milliamps", "milliCandela",
							"Millihertz", "Milliliters per Second", "Millimeters of Water",
							"Millimeters per Second", "Millimeters Water Per Square Centimeter",
							"Millinewton Meters", "millirad", "Milliwatts per Centimeters Squared", "Mils",
							"Minutes per Liter", "ml/min", "mm Hg", "Mohs", "Moldovan leu", "Mongolian tögrög",
							"Moroccan dirham", "Mozambican metical", "MP", "mscp", "mV/g", "mV/Pa", "mV/psi",
							"Myanma kyat", "Namibian dollar", "Nanoamps", "Nanofarad", "Nanoliters",
							"Nanoliters per Minute", "Nanoliters per Second", "Nanoohms", "Nanovolts", "Nanowatts",
							"Nepalese rupee", "Netherlands Antillean guilder", "New Taiwan dollar",
							"New Zealand dollar", "Newton Centimeters", "Newton Meters", "Newton Millimeters",
							"Newtons Per Square Millimeter (N/mm2)", "nH", "Nicaraguan córdoba", "Nigerian naira",
							"North Korean won", "Norwegian krone", "Ohms Per Centimeter", "Ohms Per Inch",
							"Ohms Per Meter", "Omani rial", "openings_per_square_cm", "openings_per_square_inch",
							"operations", "Ounces Per Cubic Inch", "Ounces Per Square Inch", "P", "P/s", "Pa",
							"Pages per month", "Pages per second", "Pages Per Sheet", "Pakistani rupee",
							"Panamanian balboa", "Papua New Guinean kina", "Paraguayan guaraní",
							"Peruvian nuevo sol", "Petafarad", "Philippine peso", "Photos", "Picoamps", "Picofarad",
							"Picoliters", "Picoliters per Minute", "Picoliters per Second", "Picoohms",
							"Picoseconds", "Picowatts", "Pictures", "pill(s)", "Pitch", "Pods", "Polish Zloty",
							"Portion(s)", "Portuguese Escudos", "Pound per Square Foot", "pounds",
							"Pounds per Cubic Foot", "Pounds per Cubic Inch", "Pounds per Cubic Yard",
							"Pounds Per Inch", "Pounds Per Square Yard", "ppb", "pph", "ppm", "ppq", "ppt",
							"Primary", "pulses", "Qatari riyal", "R-value", "R-value (Metric)",
							"Radians per Second Squared", "Revolutions per Second Squared", "revs", "rockwell 15N",
							"rockwell 15T", "rockwell 30N", "rockwell 30T", "rockwell 45N", "rockwell 45T",
							"Rockwell A", "Rockwell B", "Rockwell C", "Rockwell D", "rockwell E", "rockwell F",
							"Rockwell G", "Rockwell H", "Rockwell K", "Rockwell L", "rockwell M", "Rockwell P",
							"rockwell R", "Rockwell S", "Rockwell V", "Romanian Leu", "Rows", "Russian rouble",
							"Russian Ruble", "Rwandan franc", "S", "Saint Helena pound", "Samoan tala",
							"Samples per Second", "Saudi Riyal", "scoop(s)", "Serbian dinar", "Seychelles rupee",
							"Shore A", "Shore B", "Shore C", "Shore D", "Shore DO", "Shore E", "Shore M", "Shore O",
							"Shore OO", "Shore OOO", "Shore OOO-S", "Shore R", "Sierra Leonean leone",
							"Singapore dollar", "Solomon Islands dollar", "Somali shilling", "Sones",
							"South African rand", "South Korean Won", "South Sudanese pound", "Spanish Pesetas",
							"SPF", "Square Centimeters", "Square Feet", "Square Meters", "Sri Lankan rupee",
							"Stops", "Sudanese pound", "Surinamese dollar", "Swazi lilangeni", "Swedish Krona",
							"Swiss Francs", "Syrian pound", "São Tomé and Príncipe dobra", "tablespoon(s)",
							"Tajikistani somoni", "Tanzanian shilling", "TB", "TB/sec", "teaspoon(s)",
							"Teen Boys US", "Teen Girls US", "Teen US", "Teeth", "Teeth per Inch",
							"Ten Thousandths Inches", "Teraohms", "TGW", "Thai baht", "Thousandths Inches",
							"Threads Per Centimeter", "Threads Per Inch", "THz", "Toddler UK", "Toddler US", "Tog",
							"Tongan paʻanga", "Tons", "Tons per Acre", "torr", "Trinidad and Tobago dollar",
							"Tunisian dinar", "Turkish Lira", "Turkmenistani manat", "Turns",
							"Turns Per Centimeter", "Turns Per Inch", "Ugandan shilling", "Ukrainian hryvnia",
							"Unidad de Valor Real", "Unisex BR", "Unisex EU", "Unisex JP",
							"United Arab Emirates dirham", "Units", "Unknown modifier", "Uruguayan peso",
							"Uzbekistan som", "V/g", "Vanuatu vatu", "Venezuelan bolívar", "Vickers",
							"Vietnamese dong", "Volt Amperes", "Volts (AC)", "Volts (DC)", "Watts per Kilogram",
							"Watts per Meter per Degree Celsius", "Watts per Meter per Kelvin",
							"West African CFA franc", "WIR Euro", "WIR Franc", "Womens AU", "Womens UK",
							"Womens US", "Words", "x", "Yemeni rial", "Yoctofarad", "Yottafarad", "Youth UK",
							"Youth US", "Zambian kwacha", "Zeptofarad", "Zettafarad", "Zimbabwe dollar", "万亿法拉",
							"伏特", "体积百分比", "像素", "兆分之一", "克", "克拉", "分", "分米", "分钟", "加仑", "助理", "十亿分之一秒", "十亿法拉",
							"十分之一法拉", "十法拉", "千伏特", "千像素", "千克", "千分之一欧", "千卡", "千欧", "千法拉", "千焦", "千瓦", "千瓦小时",
							"千瓦小时每年", "千米", "千米", "千米/时", "升", "华氏度", "单位酒精", "卡路里", "厘升/秒", "厘米", "厘米千克", "周",
							"品脱", "圈", "夸脱", "字节", "季", "安培", "安培时", "安瓿", "小时", "小时", "层", "巴", "平方厘米", "平方厘米/秒",
							"平方英寸", "年", "度", "度/秒", "弧度", "微伏", "微秒", "微米", "拉德/秒", "摄氏度", "放置设置", "日", "月", "杯子",
							"标准大气压 (Atm)", "欧", "每 100 个周期的千瓦时", "每 1000 小时的千瓦时", "每分钟转数", "每分钟页数", "每日建议摄取百分比",
							"每瓦特转数", "每秒钟转数", "每英寸像素", "每英寸点数", "每英寸线数", "毫伏", "毫克", "毫升", "毫巴", "毫法拉", "毫瓦", "毫秒",
							"毫米", "法拉", "液量盎司", "点", "焦耳", "牛顿", "瓦", "瓦特时", "百万次每秒", "百万法拉", "百分之一法拉", "百分之一磅",
							"百分之一英寸", "百分比", "百法拉", "皮秒计", "盎司", "码", "磅每平方英寸", "秒", "立方厘米", "立方码", "立方米", "立方米每分",
							"立方米每秒", "立方英尺", "立方英尺每分", "立方英尺每秒", "章", "等级", "米", "米", "纳米", "美元", "胶囊", "英制热单位",
							"英寸", "英尺", "英尺/秒", "英里", "英里", "英里/时", "菲律宾", "角秒", "转", "转/月", "转小时", "重量百分数",
							"阿托法拉 (Attofarad)", "页"
						]
					},
					"value": {
						"title": "容量",
						"description": "提供商品的容量（用数值表示）",
						"editable": true,
						"hidden": false,
						"examples": ["7、16"],
						"type": "string",
						"maxLength": 2200
					}
				},
				"additionalProperties": false
			}
		},
		"pattern": {
			"title": "图案",
			"description": "说明商品最醒目的重复装饰设计",
			"examples": ["花卉、几何、波尔卡圆点"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "图案",
						"description": "说明商品最醒目的重复装饰设计",
						"editable": true,
						"hidden": false,
						"examples": ["花卉、几何、波尔卡圆点"],
						"type": "string",
						"maxLength": 2200,
						"maxUtf8ByteLength": 2000,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["中圆点花", "人字形图案", "佩斯利漩涡花纹", "几何", "动物打印", "卡通画", "心脏", "方格", "星型", "条纹",
								"格子花呢", "格子花纹", "水波纹", "纯色", "花饰", "菱形花纹", "迷彩", "雪佛兰"
							],
							"enumNames": ["中圆点花", "人字形图案", "佩斯利漩涡花纹", "几何", "动物打印", "卡通画", "心脏", "方格", "星型",
								"条纹", "格子花呢", "格子花纹", "水波纹", "纯色", "花饰", "菱形花纹", "迷彩", "雪佛兰"
							]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"is_expiration_dated_product": {
			"title": "产品是否过期",
			"description": "如果产品满足以下条件之一，请选择“是”：1.有打印的有效期；2.是供人类或动物使用的外用品或消耗品；3.有建议的保质期。否则，选择“否”。",
			"examples": ["是，没有"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品是否过期",
						"description": "提供产品是否具有有效期",
						"editable": true,
						"hidden": false,
						"examples": ["是，没有"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					}
				},
				"additionalProperties": false
			}
		},
		"product_expiration_type": {
			"title": "产品保质期类型",
			"description": "说明商品的到期类型。到期类型指明如何或从何时开始确定商品的到期时间。",
			"examples": ["包装到期、未到期"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品保质期类型",
						"description": "说明商品的到期类型。到期类型指明如何或从何时开始确定商品的到期时间。",
						"editable": true,
						"hidden": false,
						"examples": ["需要保质期、货架期"],
						"type": "string",
						"enum": ["Does Not Expire", "Expiration Date Required", "Expiration On Package",
							"Production Date Required", "Shelf Life"
						],
						"enumNames": ["Does Not Expire", "Expiration Date Required", "Expiration On Package",
							"Production Date Required", "Shelf Life"
						],
						"maxLength": 555
					}
				},
				"additionalProperties": false
			}
		},
		"fc_shelf_life": {
			"title": "营运中心保质期",
			"description": "说明商品在到期前可以存放的总天数。配送技术要求该值以天为单位。",
			"examples": ["180 天、365 天"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["unit", "value"],
				"properties": {
					"value": {
						"title": "营运中心保质期",
						"description": "说明商品在到期前可以存放的总天数。配送技术要求该值以天为单位。",
						"editable": false,
						"hidden": false,
						"examples": ["180、365"],
						"type": "number",
						"minimum": 0,
						"maximum": 1825,
						"maxLength": 5000
					},
					"unit": {
						"title": "营运中心保质期单位",
						"description": "说明商品在到期前可以存放的总天数。配送技术要求该值以天为单位。",
						"editable": false,
						"hidden": false,
						"examples": ["天数"],
						"type": "string",
						"enum": ["days"],
						"enumNames": ["日"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"item_form": {
			"title": "商品质地",
			"description": "请简要说明该商品的质地。",
			"examples": ["热装罐"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "商品质地",
						"description": "请简要说明该商品的质地。",
						"editable": true,
						"hidden": false,
						"examples": ["热装罐"],
						"type": "string",
						"maxLength": 1100,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["丝带", "双钩发绳", "大肠发圈", "电话线"],
							"enumNames": ["丝带", "双钩发绳", "大肠发圈", "电话线"]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"unit_count": {
			"title": "单位计数",
			"description": "指定产品的单位数量和单位类型",
			"examples": ["各 5 个"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["type", "value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"type": {
						"title": "单位计数类型",
						"description": "指定可让客户做出购买决定的单位类型。一瓶苏打水可以有各种尺寸，以液盎司为单位计量；一盒螺钉用盒中螺钉的个数来计量。",
						"examples": ["每个"],
						"type": "object",
						"required": ["language_tag", "value"],
						"properties": {
							"language_tag": {
								"$ref": "#/$defs/language_tag"
							},
							"value": {
								"title": "单位计数类型",
								"description": "指定可让客户做出购买决定的单位类型。一瓶苏打水可以有各种尺寸，以液盎司为单位计量；一盒螺钉用盒中螺钉的个数来计量。",
								"editable": true,
								"hidden": false,
								"examples": ["个, 克"],
								"type": "string",
								"enum": ["个"],
								"enumNames": ["个"],
								"maxLength": 100,
								"maxUtf8ByteLength": 20
							}
						},
						"additionalProperties": false
					},
					"value": {
						"title": "单位计数",
						"description": "指定产品的单位数量可用于计算产品的每单位价格。",
						"editable": true,
						"hidden": false,
						"examples": ["5"],
						"type": "number",
						"minimum": 0
					}
				},
				"additionalProperties": false
			}
		},
		"product_site_launch_date": {
			"title": "发布日期",
			"description": "当您要对之前已发布过的商品进行上传时，请在该输入项内指定该商品在网站上发布的日期； 当您要对之前未发布过的商品进行上传或测试时，请将该日期设置为一年之后。当您准备好要正式发布该商品时，我们将针对如何设置发布日期，为您提供具体说明。",
			"examples": ["2004-08-18"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品网站发布日期",
						"description": "提供产品发布的日期，应首先显示在亚马逊网站上（YYYY-MM-DD 格式）。PSLD（产品网站发布日期）不会影响购买性或预订逻辑性，它用于表示产品何时在亚马逊网站上可见和可搜索",
						"editable": true,
						"hidden": false,
						"examples": ["2017-07-20"],
						"type": "string",
						"oneOf": [{
							"type": "string",
							"format": "date"
						}, {
							"type": "string",
							"format": "date-time"
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"specific_uses_for_product": {
			"title": "特殊用途",
			"description": "该商品的推荐用途是什么？",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 37,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "产品的具体用途",
						"description": "从建议值列表中选择产品专门针对的条件或用途。",
						"editable": true,
						"hidden": false,
						"examples": ["干式打磨、个人、商业、业余"],
						"type": "string",
						"maxLength": 500
					}
				},
				"additionalProperties": false
			}
		},
		"league_name": {
			"title": "联赛名称",
			"description": "给出与产品相关的联赛名称",
			"examples": ["NBA"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 6,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "联赛名称",
						"description": "给出与产品相关的联赛名称",
						"editable": true,
						"hidden": false,
						"examples": ["NBA"],
						"type": "string",
						"maxLength": 100,
						"maxUtf8ByteLength": 50,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["LPGA", "MLB", "MLS", "NBA", "NCAA", "NFL", "NHL", "NWSL", "PGA", "UFC",
								"WNBA", "WUSA", "WWE", "一级方程式锦标赛（赛车）", "世界棒球经典赛", "全国长曲棍球联盟", "加拿大超级联赛",
								"加拿大足球联赛", "卡巴迪职业联赛", "印度曲棍球联赛", "印度板球超级联赛", "印度羽毛球超级联赛", "印度足球甲级联赛",
								"印度足球联赛", "国家队", "国际板球赛", "墨西哥足球协会", "德国足球", "意大利足球", "日本棒球", "日本篮球",
								"日本足球", "更多足球联赛", "极限运动", "欧洲篮球", "法国足球", "电子竞技", "纳斯卡大奖赛（赛车）", "网球",
								"美国职棒小联盟（MLB，Minor League Baseball）", "职业排球联赛", "职业摔跤联赛", "苏格兰足球", "英格兰足球",
								"荷兰足球", "葡萄牙足球", "西班牙足球", "足球世界杯"
							],
							"enumNames": ["LPGA", "MLB", "MLS", "NBA", "NCAA", "NFL", "NHL", "NWSL", "PGA",
								"UFC", "WNBA", "WUSA", "WWE", "一级方程式锦标赛（赛车）", "世界棒球经典赛", "全国长曲棍球联盟",
								"加拿大超级联赛", "加拿大足球联赛", "卡巴迪职业联赛", "印度曲棍球联赛", "印度板球超级联赛", "印度羽毛球超级联赛",
								"印度足球甲级联赛", "印度足球联赛", "国家队", "国际板球赛", "墨西哥足球协会", "德国足球", "意大利足球", "日本棒球",
								"日本篮球", "日本足球", "更多足球联赛", "极限运动", "欧洲篮球", "法国足球", "电子竞技", "纳斯卡大奖赛（赛车）",
								"网球", "美国职棒小联盟（MLB，Minor League Baseball）", "职业排球联赛", "职业摔跤联赛", "苏格兰足球",
								"英格兰足球", "荷兰足球", "葡萄牙足球", "西班牙足球", "足球世界杯"
							]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"team_name": {
			"title": "球队名称",
			"description": "请输入产品的球队名称",
			"examples": ["西雅图海鹰，威斯康星獾队"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 2,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "球队名称",
						"description": "请输入产品的球队名称",
						"editable": true,
						"hidden": false,
						"examples": ["西雅图海鹰，威斯康星獾队"],
						"type": "string",
						"maxLength": 100,
						"maxUtf8ByteLength": 50,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["1.科隆足球俱乐部", "1_fc_magdeburg", "A. J. Allmendinger", "A.S.利沃诺足球俱乐部",
								"AC 米兰足球俱乐部", "ADO 海牙足球俱乐部", "airdrieonians", "AISIN AW AREIONS ANJO",
								"AKITA NORTHERN HAPPINETS", "Alex Bowman", "almere_city", "ALVARK TOKYO",
								"AOMORI WAT'S", "arbroath", "Aric Almirola", "Aston Martin", "ASVEL篮球队",
								"ATK 莫亨巴根足球俱乐部", "atletico_de_san_luis", "Austin Dillon", "BAMBITIOUS NARA",
								"Bobby Labonte", "Brad Keselowski", "BWT 赛点车队", "C.D.美国芝华士足球俱乐部",
								"Carl Edwards", "Casey Mears", "Chase Elliott", "CHIBA JETS",
								"Chris Buescher", "clermont_foot_63", "Clint Bowyer", "Dale Earnhardt",
								"Dale Earnhardt Jr.", "Daniel Suárez", "darmstadt_98",
								"Darrell Wallace Jr.", "David Ragan", "DC联队", "Denny Hamlin",
								"EARTHFRIENDS TOKYO Z", "EHIME ORANGE VIKINGS", "Elliott Sadler",
								"Erik Jones", "F EAGLES NAGOYA", "F.C.岐阜足球俱乐部", "fc_heidenheim",
								"fc_juarez", "fc_volendam", "Fiji", "fortuna_sittard",
								"FUKUSHIMA FIREBONDS", "Gray Gaulding", "Greg Biffle", "greuther_furth",
								"GUNMA CRANE THUNDERS", "HIROSHIMA DRAGONFLIES", "IBARAKI ROBOTS",
								"inverness_caledonian_thistle", "IWATE BIG BULLS", "Jamie McMurray",
								"Jeff Burton", "Jeff Gordon", "Jimmie Johnson", "Joey Logano",
								"Juan Pablo Montoya", "KAGAWA FIVE ARROWS", "KAGOSHIMA REBNISE",
								"KANAZAWA SAMURAIZ", "karlsruher_sc", "Kasey Kahne",
								"KAWASAKI BRAVE THUNDERS", "Kevin Harvick", "KUMAMOTO VOLTERS",
								"Kurt Busch", "Kyle Busch", "Kyle Larson", "KYOTO HANNARYZ",
								"LEVANGA HOKKAIDO", "LPGA", "Mark Martin", "Martin Truex Jr.",
								"Matt DiBenedetto", "Matt Kenseth", "mazatlan", "Mclaren",
								"Michael McDowell", "NAC 布雷达足球俱乐部", "NAGOYA DIAMOND DOLPHINS", "Nauru",
								"NCR 旁遮普皇家队", "NEC 奈梅亨足球俱乐部", "necaxa", "NEROCA FC", "NIIGATA ALBIREX BB",
								"NISHINOMIYA STORKS", "norwich", "OSAKA EVESSA",
								"OTSUKA CORPORATION ALPHAS", "Paul Menard", "PGA", "PSG", "PSV 埃因霍温足球俱乐部",
								"QPR", "queretaro", "RBC 罗辛达足球俱乐部", "Red Bull Racing", "Richard Petty",
								"Ricky Stenhouse Jr.", "RIZING ZEPHYR FUKUOKA", "Roma", "ross_county",
								"Ryan Blaney", "Ryan Newman", "RYUKYU GOLDEN KINGS", "SAITAMA BRONCOS",
								"SAN-EN NEOPHOENIX", "SEAHORSES MIKAWA", "SENDAI EIGHTY NINERS",
								"SHIGA LAKESTARS", "SHIMANE SUSANOO MAGIC", "SHINSHU BRAVE WARRIORS",
								"sparta_rotterdam", "st_johnstone", "st_mirren", "SUNROCKERS SHIBUYA",
								"TJ·迪拉肖", "TOCHIGI BREX", "TOKIO MARINE NICHIDO BIG BLUE",
								"TOKYO CINQ REVES", "TOKYO EXCELLENCE", "TOKYO HACHIOJI TRAINS", "Tonga",
								"Tony Stewart", "TOYAMA GROUSES", "TOYODA GOSEI SCORPIONS", "Trevor Bayne",
								"Tuvalu", "Ty Dillon", "union_berlin", "UP 丹加勒队", "vfl_osnabrück",
								"William Byron", "WUSA", "WWE", "YAMAGATA WYVERNS", "YOKOHAMA B-CORSAIRS",
								"York9 FC", "三城谷猫队", "三市尘埃魔鬼队", "不丹", "不列颠哥伦比亚雄狮队", "丘吉尔兄弟足球俱乐部",
								"东京养乐多燕子队", "东京绿茵足球俱乐部", "东伊利诺伊大学黑豹队", "东北乐天金鹫队", "东北伊利诺伊州金鹰队", "东北勇士队",
								"东北大学哈士奇队", "东北联足球俱乐部", "东华盛顿大学老鹰队", "东南密苏里州印第安人队", "东南密苏里州州立大学红鹰队",
								"东卡罗来纳大学海盗队", "东密歇根大学老鹰队", "东帝汶", "东田纳西州州立大学海盗队", "东肯塔基大学上校队",
								"中佛罗里达大学金骑士队", "中华台北", "中国", "中密歇根大学奇普瓦斯队", "中康涅狄格大学蓝魔队", "中日龙队",
								"中田纳西州立大学蓝色突袭者队", "中量级", "中非共和国", "丹·胡克", "丹佛大学先驱者队", "丹佛掘金队", "丹佛野马队",
								"丹尼尔·科米尔", "丹维尔勇士队", "丹麦", "丽兹·卡穆什", "丽娜·兰斯伯格", "乌克兰", "乌兹别克斯坦", "乌利亚·霍尔",
								"乌姆巴排球对", "乌姆巴队", "乌干达", "乌德勒支足球俱乐部", "乌拉圭", "乌迪内斯足球俱乐部", "乍得", "乔什·埃米特",
								"乔安妮·考尔德伍德", "乔安娜·耶德尔泽奇克", "乔恩·琼斯", "乔治·圣皮埃尔", "乔治亚南方大学鹰队", "乔治亚斗牛犬队",
								"乔治亚理工大黄蜂队", "乔治亚蜂群队", "乔治华盛顿殖民者队", "乔治城大学惊叹队", "乔治梅森大学爱国者队", "也门",
								"云达不莱梅足球俱乐部", "亚利桑那响尾蛇队", "亚利桑那大学野猫队", "亚利桑那州立大学太阳魔队", "亚利桑那红雀队", "亚利桑那郊狼队",
								"亚历克斯·佩雷斯", "亚历克斯·奥利维拉", "亚历克萨·格拉索", "亚历克西斯·戴维斯", "亚历山大·古斯塔夫森",
								"亚历山大·埃尔南德斯", "亚历山大·沃尔卡诺斯基", "亚历山大·沃尔科夫", "亚历山大·潘托哈", "亚历杭德罗·佩雷斯",
								"亚娜·库尼茨卡娅", "亚尔·罗德里格斯", "亚特兰大勇士队", "亚特兰大梦想队", "亚特兰大火焰队", "亚特兰大猎鹰队",
								"亚特兰大老鹰队", "亚特兰大联足球俱乐部", "亚特兰大足球俱乐部", "亚特兰大鸫鸟队", "亚眠竞技俱乐部", "亚美尼亚",
								"亨利·塞胡多", "亨茨维尔星队", "京都不死鸟足球俱乐部", "仙台七夕足球队", "代托纳小熊队", "以色列", "伊利尔·拉提菲",
								"伊利海狼队", "伊利莎白顿双城队", "伊利诺伊大学伊利尼队", "伊利诺伊大学芝加哥分校火焰队", "伊利诺伊州立大学红雀队", "伊拉克",
								"伊斯雷尔·阿德桑亚", "伊普斯维奇城足球俱乐部", "伊朗", "伊隆大学凤凰队", "伍尔弗汉普顿流浪者足球俱乐部", "休斯顿大学美洲狮队",
								"休斯顿太空人队", "休斯顿彗星队", "休斯顿德州人队", "休斯顿浸会大学哈士奇队", "休斯顿火箭队", "休斯顿达斯足球俱乐部",
								"休斯顿迪纳摩队", "伦秋库卡蒙佳地震队", "伯利兹", "伯恩利足球俱乐部", "伯恩茅斯足球俱乐部", "伯明翰城足球俱乐部",
								"伯明翰男爵队", "伯灵顿皇家队", "伯灵顿蜜蜂队", "但尼丁蓝鸟队", "佐佐木佑太", "佐治亚州立大学黑豹队", "体育足球俱乐部",
								"何塞·奥尔多", "佛得角", "佛罗伦萨足球俱乐部", "佛罗里达农工大学响尾蛇队", "佛罗里达国际大学金豹队", "佛罗里达国际大学黑豹队",
								"佛罗里达大学短吻鳄队", "佛罗里达大西洋猫头鹰队", "佛罗里达州立大学塞米诺尔队", "佛罗里达海湾海岸鹰队", "佛罗里达美洲豹队",
								"佛蒙特大山猫队", "佛蒙特湖怪兽队", "佩德罗·蒙霍兹", "佩珀代因大学波浪队", "佩鲁贾足球俱乐部", "依云足球俱乐部",
								"俄亥俄大学山猫队", "俄亥俄州立大学七叶树队", "俄克拉荷马城红鹰队", "俄克拉荷马大学捷足者队", "俄克拉荷马州立大学牛仔队",
								"俄克拉荷马雷霆队", "俄勒冈大学鸭子队", "俄勒冈州立大学海狸队", "俄罗斯", "保加利亚", "保罗·科斯塔", "保罗·费尔德",
								"克利夫兰州立维京人队", "克利夫兰布朗队", "克利夫兰男爵队", "克利夫兰纳普斯队", "克利夫兰蓝队", "克利夫兰骑士队",
								"克劳迪娅·加德哈", "克林顿伐木王队", "克罗地亚", "克莱姆森大学老虎队", "克赖顿大学蓝鸟队", "克里夫兰印地安人队",
								"克里夫兰摇滚者队", "克里斯·赛博格", "克里斯·魏德曼", "兰契雷曲棍球队", "兰开斯特喷射鹰队", "兰斯足球俱乐部", "兰辛螺帽队",
								"兰达·马科斯", "关岛", "兹沃勒足球俱乐部", "内华达大学拉斯维加斯分校叛逆者队", "内华达大学狼群队", "内布拉斯加大学剥玉米人队",
								"内特·迪亚兹", "内陆帝国 66 人队", "冈山绿雉足球俱乐部", "冈比亚", "冈萨加大学斗牛犬队", "冰岛", "几内亚",
								"几内亚比绍", "凯伦·维埃拉", "凯尔特人足球俱乐部", "凯恩县美洲狮队", "凯文·李", "凯文·盖斯特鲁姆", "凯泽斯劳滕足球俱乐部",
								"凯特·辛加诺", "凯特琳·乔卡吉安", "切尔西靴", "切沃维罗纳足球俱乐部", "列克星敦传奇队", "列支敦士登", "刚果",
								"刚果民主共和国", "利伯缇大学火焰队", "利兹联足球俱乐部", "利哈伊山大学老鹰队", "利文斯顿足球俱乐部", "利比亚", "利比里亚",
								"利物浦", "前进之鹰足球俱乐部", "加尔各答骑士团队", "加州大学圣巴巴拉分校高乔人队", "加州大学尔湾分校食蚁兽队",
								"加州大学戴维斯分校队", "加州大学河滨高地人队", "加州大学洛杉矶分校棕熊队", "加州天使队", "加州州立大学北岭斗牛士队",
								"加州州立大学富勒顿泰坦队", "加州州立大学萨克拉门托黄蜂队", "加州州立大学贝克斯菲尔德路跑者队", "加州海豹队", "加州浸会大学兰瑟斯队",
								"加州理工大学野马队", "加州金熊队", "加德纳韦伯鲁宁斗牛犬队", "加拉塔萨雷足球俱乐部", "加拿大", "加的斯足球俱乐部", "加纳",
								"加蓬", "努曼西亚足球俱乐部", "劳伦·墨菲增加 1", "勒阿弗尔足球俱乐部", "匈牙利", "北九州向日葵队",
								"北亚利桑那大学伐木工人队", "北伊利诺斯大学哈士奇队", "北佛罗里达大学鱼鹰队", "北卡罗来纳中央老鹰队", "北卡罗来纳农工大学队",
								"北卡罗来纳勇气队", "北卡罗来纳大学夏洛特分校 49 人队", "北卡罗来纳大学威尔明顿分校海鹰队", "北卡罗来纳大学格林斯伯勒分校斯巴达人队",
								"北卡罗来纳州立大学狼群队", "北卡罗来纳州阿什维尔斗牛犬队", "北卡罗莱纳大学焦油踵队", "北德克萨斯大学蛮横格林队", "北方邦奇才队",
								"北海道日本火腿斗士队", "北爱荷华大学黑豹队", "北科罗拉多熊队", "北肯塔基大学挪威人队", "北达科他大学战斗鹰队",
								"北达科他大学斗鹰队", "北达科他大学队", "北达科他州立大学野牛队", "北阿拉巴马狮子队", "匹兹堡企鹅队", "匹兹堡大学黑豹队",
								"匹兹堡海盗队", "匹兹堡钢人队", "千叶罗德海洋队", "华盛顿国民队", "华盛顿大学哈士奇队", "华盛顿奇才队",
								"华盛顿州立大学美洲狮队", "华盛顿指挥官", "华盛顿神秘人队", "华盛顿精神足球俱乐部", "华盛顿美式足球队", "华盛顿首都队",
								"南伊利诺伊大学萨路基猎犬队", "南伊利诺大学爱德华城美洲狮队", "南佛罗里达大学公牛队", "南加州大学北部分校斯巴达人队",
								"南加州大学特洛伊人队", "南卡罗来纳州大学斗鸡队", "南卡罗来纳州立大学斗牛犬队", "南卫理工会大学野马队", "南安普敦足球俱乐部",
								"南密西西比大学金鹰队", "南弯银鹰队", "南斯拉夫", "南方大学美洲虎队", "南特足球俱乐部", "南犹他大学雷鸟队", "南苏丹",
								"南达科他大学郊狼队", "南达科他州立大学野兔队", "南阿拉巴马大学美洲虎队", "南非", "博伊西州立大学野马队", "博伊西老鹰队",
								"博卡青年队", "博尔顿足球俱乐部", "博洛尼亚足球俱乐部", "博茨瓦纳", "博阿维斯塔足球俱乐部", "卡利亚里足球俱乐部",
								"卡利卡特英雄队", "卡哈比·努尔马格妙多夫", "卡坦扎罗", "卡塔尔", "卡塔尼亚足球俱乐部", "卡尔加里火焰队", "卡尔加里牛仔队",
								"卡尔加里罗内克斯队", "卡尔加里骑兵足球俱乐部", "卡尼修斯学院金狮鹫兽队", "卡布·斯旺森", "卡拉·埃斯帕萨", "卡斯柏小鬼队",
								"卡昂足球俱乐部", "卡林加枪骑兵队", "卡罗琳娜·科沃凯维奇", "卡罗莱纳泥鳅队", "卡罗莱纳海岸大学唱诗班队", "卡罗莱纳飓风队",
								"卡罗莱纳黑豹队", "卡萨皮亚", "卡迪夫城足球俱乐部", "卡马鲁·乌斯曼", "卢克·洛克霍尔德", "卢戈足球俱乐部", "卢旺达",
								"卢森堡", "卢顿足球俱乐部", "印度尼西亚", "印度音乐", "印第安纳大学山地人队", "印第安纳大学美洲虎队",
								"印第安纳大学－普渡大学韦恩堡分校乳齿象队", "印第安纳州梧桐队", "印第安纳步行者队", "印第安纳波利斯印地安人队",
								"印第安纳波利斯小马队", "印第安纳狂热队", "印第安阿罗足球俱乐部", "危地马拉", "厄瓜多尔", "厄立特里亚", "叙利亚",
								"古吉拉特狮队", "古吉拉特邦巨星队", "古巴", "古巴队", "古库拉姆喀拉拉邦足球俱乐部", "史坦顿岛洋基队", "吉尔吉斯斯坦",
								"吉尔维森特足球俱乐部", "吉布提", "吉米·里维拉", "吉米·马努瓦", "吉马良斯维多利亚足球俱乐部", "名古屋鲸鱼队",
								"哈佛大学深红队", "哈利法克斯流浪者足球俱乐部", "哈利法克斯雷鸟队", "哈密尔顿锻钢足球俱乐部", "哈德斯菲尔德足球俱乐部",
								"哈德逊河谷叛徒队", "哈斯 F1 车队", "哈特福德大学老鹰队", "哈特福德捕鲸人队", "哈萨克斯坦", "哈里亚纳邦铁锤队",
								"哈里亚纳钢人队", "哈里斯堡参议员队", "哥伦布快船队", "哥伦布机员足球俱乐部", "哥伦布蓝衣队", "哥伦比亚",
								"哥伦比亚大学狮子队", "哥斯达黎加", "唐纳德·塞罗内", "喀拉拉爆破手足球俱乐部", "喀麦隆", "国王十一旁遮普队",
								"国立自治大学俱乐部- 美洲狮", "国际米兰", "图卢兹足球俱乐部", "图拉兵工厂足球俱乐部", "圆石城特快车队", "土库曼斯坦",
								"圣何塞地震队", "圣何塞州立大学斯巴达人队", "圣何塞巨人队", "圣何塞鲨鱼队", "圣保利足球俱乐部", "圣保罗足球俱乐部",
								"圣克拉拉足球俱乐部", "圣克拉拉野马队", "圣十字学院十字军队", "圣博纳旺蒂尔邦尼队", "圣卢西亚", "圣地亚哥·庞齐尼比奥",
								"圣地亚哥大学斗牛士队", "圣地亚哥州立大学阿兹台克人队", "圣地亚哥快船队", "圣地亚哥教士队", "圣地亚哥波浪足球俱乐部",
								"圣地亚哥海豹队", "圣地亚哥火箭队", "圣埃蒂安足球俱乐部", "圣基茨和尼维斯", "圣多美和普林西比", "圣安东尼奥使命队",
								"圣安东尼奥银星队", "圣安东尼奥马刺队", "圣弗朗西斯大学（宾夕法尼亚州）红色闪光队", "圣弗朗西斯学院（纽约）梗犬队",
								"圣弗朗西斯布鲁克林梗队", "圣彼得大学孔雀队", "圣心先锋队", "圣文森特和格林纳丁斯", "圣母大学爱尔兰战士队", "圣玛丽大学盖尔人队",
								"圣玛丽学院响尾蛇队", "圣玛丽山大学登山者队", "圣约瑟夫老鹰队", "圣约翰红色风暴队", "圣路易斯城", "圣路易斯大学比利肯斯队",
								"圣路易斯红雀队", "圣路易斯老鹰队", "圣路易斯蓝调队", "圣路易斯轰炸机队", "圣道学院红雀队", "圣露西大都会队", "圣马力诺",
								"圭亚那", "坎布尔足球俱乐部", "坎纳波利斯大炮队", "坎贝尔大学战斗骆驼队", "坦帕洋基队", "坦帕湾光芒队", "坦帕湾海盗队",
								"坦帕湾闪电队", "坦桑尼亚", "埃利亚斯·西奥多鲁", "埃利泽·扎莱斯基·多斯桑托斯", "埃及", "埃塞俄比亚", "埃尔切足球俱乐部",
								"埃弗顿足球俱乐部", "埃德森·巴博萨", "埃德蒙顿油人队", "埃德蒙顿足球俱乐部", "埃德蒙顿足球队", "埃文斯维尔紫色王牌队",
								"埃斯托里尔", "埃斯特雷马杜拉足球俱乐部", "埃瓦尔足球俱乐部", "基尔马诺克足球俱乐部", "基里巴斯", "埼玉西武狮队",
								"堪萨斯城体育会队", "堪萨斯城国王队", "堪萨斯城潮流", "堪萨斯城田径队", "堪萨斯城皇家队", "堪萨斯城童子军队",
								"堪萨斯城酋长队", "堪萨斯大学杰鹰队", "堪萨斯州立大学野猫队", "塔可马雨人队", "塔吉克斯坦", "塔尔萨大学金色飓风队",
								"塔尔萨钻探者队", "塔尔萨震动队", "塔蒂亚娜·苏亚雷斯", "塞勒姆凯泽火山队", "塞图巴尔维多利亚足球俱乐部", "塞尔吉奥·佩蒂斯",
								"塞尔维亚", "塞尔维亚和黑山", "塞拉利昂", "塞浦路斯", "塞维利亚足球俱乐部", "塞舌尔", "塞顿霍尔大学海盗队", "墨西哥",
								"墨里西欧·鲁阿", "夏威夷大学彩虹勇士队", "夏洛特大学 49 人队", "夏洛特石蟹队", "夏洛特针刺队", "夏洛特骑士队",
								"夏洛特黄蜂队", "多伦多圣帕茨队", "多伦多摇滚队", "多伦多枫叶队", "多伦多淘金人足球队", "多伦多猛龙队", "多伦多竞技场队",
								"多伦多蓝鸟队", "多伦多足球俱乐部", "多哥", "多特蒙德足球俱乐部", "多米尼克·克鲁兹", "多米尼克·雷耶斯", "多米尼加",
								"多米尼加共和国", "多米尼加共和国队", "大分三神足球队", "大卫·布兰奇", "大宫松鼠足球俱乐部", "大峡谷大学羚羊队",
								"大湖潜鸟队", "大瀑布航海者队", "大邦孟买 HC 队", "大邦德里队", "大阪樱花队", "大阪钢巴足球俱乐部", "天使城足球俱乐部",
								"天普大学猫头鹰队", "太平洋大学拳击手队", "太平洋大学老虎队", "太平洋足球俱乐部", "奇塔代拉", "奎德城河盗队",
								"奥克兰大学金熊队", "奥克兰突袭者队", "奥克兰运动家队", "奥兰多城队", "奥兰多荣耀足球队", "奥兰多魔术队",
								"奥列克西·奥利尼克", "奥古斯塔格林杰克斯队", "奥地利", "奥尔巴尼大丹尼斯队", "奥尔康州立勇士队", "奥文斯·圣普勒",
								"奥斯汀皮埃州长队", "奥斯汀足球俱乐部", "奥本大学老虎队", "奥本达博岱队", "奥林匹亚科斯篮球俱乐部", "奥格斯堡足球俱乐部",
								"奥格登猛龙队", "奥罗尔罗伯茨大学金鹰队", "奥萨苏纳足球俱乐部", "奥里萨邦足球俱乐部", "奥马哈小牛队", "奥马哈风暴追赶者队",
								"委内瑞拉", "委内瑞拉队", "威克森林大学恶魔指挥官队", "威奇托州立大学震撼者队", "威尔士", "威尔明顿蓝岩队", "威尔逊·雷斯",
								"威尼斯足球俱乐部", "威廉姆斯车队", "威廉波特横锯队", "威廉玛丽学院部落队", "威斯康星大学绿湾凤凰队", "威斯康星密尔沃基黑豹队",
								"威斯康星獾队", "威斯康辛木纹响尾蛇队", "威根竞技足球俱乐部", "孟买印度人队", "孟买城足球俱乐部", "孟买火箭队",
								"孟买马哈拉蒂队", "孟加拉勇士队", "孟加拉国", "孟德斯托坚果队", "孟菲斯灰熊队", "孟菲斯红鸟队", "孟菲斯老虎队",
								"安东尼·佩蒂斯", "安东尼·史密斯", "安吉拉·希尔", "安哥拉", "安德烈·阿尔洛夫斯基", "安德里亚·李", "安提瓜和巴布达",
								"安纳托利亚艾菲斯伊斯坦布尔篮球队", "安纳海姆鸭队", "安道尔", "宾厄姆顿大都会队", "宾夕法尼亚大学贵格会队",
								"宾州州立大学尼塔尼雄狮队", "宾汉顿大学熊狸队", "密克罗尼西亚联邦", "密尔沃基勇士队", "密尔沃基老鹰队", "密尔沃基雄鹿队",
								"密尔瓦基酿酒人队", "密歇根大学狼獾队", "密歇根州立斯巴达人队", "密苏里大学堪萨斯城分校袋鼠队", "密苏里大学老虎队",
								"密苏里州鱼鹰队", "密西西比勇士队", "密西西比大学反叛者队", "密西西比大学叛逆者队", "密西西比州立大学斗牛犬队",
								"密西西比河谷州三角洲魔鬼队", "富勒姆足球俱乐部", "富山胜利足球队", "富恩拉布拉达足球俱乐部", "小安东尼奥·卡洛斯", "小轿车",
								"尤文图斯足球俱乐部", "尤西尔·福米加", "尤达队", "尤金绿宝石队", "尼亚加拉大学紫鹰队", "尼加拉瓜", "尼可·蒙塔尼奥",
								"尼基塔·克雷洛夫", "尼姆奥林匹克足球俱乐部", "尼娜·安萨罗夫", "尼尔·马格尼", "尼斯足球俱乐部", "尼日利亚", "尼日尔",
								"尼泊尔", "尼科尔斯州立大学上校队", "山口雷法足球俱乐部", "山形山神足球俱乐部", "岩手盛冈仙鹤足球俱乐部", "川崎前锋队",
								"州立大学尖峰队", "巴克内尔大学野牛队", "巴列卡诺足球俱乐部", "巴利亚多利德足球俱乐部", "巴勒斯坦", "巴勒莫", "巴哈马",
								"巴基斯坦", "巴塔维亚脏狗队", "巴塞尔足球俱乐部", "巴塞罗那篮球俱乐部", "巴塞罗那足球俱乐部", "巴尔的摩乌鸦队",
								"巴尔的摩金莺队", "巴巴多斯", "巴布亚新几内亚", "巴拉圭", "巴拿马草帽", "巴拿马队", "巴斯干尼亚篮球队",
								"巴斯蒂亚足球俱乐部", "巴林", "巴特勒大学斗牛犬队", "巴特那海盗队", "巴西", "巴里", "市原千叶足球俱乐部",
								"布伦特福德足球俱乐部", "布伦瑞克足球俱乐部", "布兰登·莫雷诺", "布基纳法索", "布拉加竞技俱乐部", "布拉德·塔瓦雷斯",
								"布拉德利勇士队", "布朗大学熊队", "布法罗大学公牛队", "布法罗比尔队", "布莱克本流浪者足球俱乐部", "布莱恩特大学斗牛犬队",
								"布莱顿足球俱乐部", "布赖恩·奥尔特加", "布里斯托尔城足球俱乐部", "布隆迪", "布雷斯特足球俱乐部", "布雷瓦德县海牛队",
								"布雷登顿掠夺者队", "布雷西亚足球俱乐部", "布鲁克林篮网队", "布鲁克林飓风队", "希伯尼安足球俱乐部", "希姆基莫斯科地区篮球队",
								"希洪竞技足球俱乐部", "希科里淡水龙虾队", "希腊", "帕劳", "帕尔蒂克足球俱乐部", "帕尔马足球俱乐部", "帕纳辛奈克斯篮球俱乐部",
								"帝王足球俱乐部", "广岛三箭足球队", "广岛东洋鲤鱼队", "库克群岛", "底特律大学泰坦队", "底特律活塞队", "底特律猎鹰队",
								"底特律红翼队", "底特律美洲狮队", "底特律老虎队", "底特律雄狮队", "底特律震动队", "庞费拉迪纳足球俱乐部", "康奈尔大红队",
								"康涅狄格大学哈士奇队", "康涅狄格太阳队", "康涅狄格州中部蓝魔队", "康涅狄格老虎队", "康纳·麦格雷戈", "开曼群岛",
								"弗兰基·埃德加", "弗吉尼亚军事学院军校生队", "弗吉尼亚大学骑士队", "弗吉尼亚理工大学霍奇队", "弗吉尼亚联邦大学公羊队",
								"弗曼大学圣骑士队", "弗朗西斯·纳干诺", "弗朗西斯科·特里纳尔多", "弗罗西诺内足球俱乐部", "弗莱堡足球俱乐部", "弗里斯科游骑兵队",
								"弗雷德里克钥匙队", "弗雷斯诺州立斗牛犬队", "弗雷斯诺灰熊队", "得州农工大学农夫队", "德保罗大学蓝色恶魔队",
								"德克萨斯农工大学科普斯克里斯蒂岛民队", "德克萨斯南方大学老虎队", "德克萨斯大学埃尔帕索矿工队", "德克萨斯大学泛美分校野马队",
								"德克萨斯大学长角牛队", "德克萨斯大学阿灵顿分校小牛队", "德克萨斯州立大学山猫队", "德克萨斯巡游者队", "德克萨斯理工大学红色突袭者队",
								"德克萨斯里奥格兰德河谷大学牛仔队", "德国", "德岛漩涡足球俱乐部", "德州基督教大学角蛙队", "德州大学圣安东尼奥分校路跑者队",
								"德比郡足球俱乐部", "德玛瓦水鸟队", "德米安·迈亚", "德累斯顿迪纳摩足球俱乐部", "德维森·菲格雷多", "德里乘波者队",
								"德里克·刘易斯", "德里克·布伦森", "德里苏丹队", "德里首都队", "德雷克大学斗牛犬队", "德雷塞尔大学龙队", "德顿龙队",
								"心脏", "怀俄明大学牛仔队", "恩波利足球俱乐部", "意大利", "慕尼黑 1860 足球俱乐部", "戴维森学院野猫队",
								"戴顿大学飞人队", "所罗门群岛", "扎比特·马戈梅德沙里波夫", "托尼·弗格森", "托尼亚·埃文格", "托特纳姆热刺足球俱乐部",
								"托莱多泥母鸡队", "托莱多火箭队", "托马斯·阿尔梅达", "扬·布拉霍维奇", "扬斯敦州立大学企鹅队", "拉奎尔·彭宁顿",
								"拉尼·叶海亚", "拉德福德大学高地人队", "拉斐尔·多斯·安霍斯", "拉斐尔·阿松桑", "拉斐特学院花豹队", "拉斯帕尔马斯足球俱乐部",
								"拉斯维加斯王牌队", "拉斯维加斯突袭者队", "拉斯维加斯飞行者队", "拉科鲁尼亚足球俱乐部", "拉脱维亚", "拉萨尔大学探险者队",
								"拉贾斯坦皇家队", "拉马尔大学红雀队", "拉齐奥", "拜仁慕尼黑篮球俱乐部", "拜仁慕尼黑足球俱乐部", "拜耳勒沃库森足球俱乐部",
								"挪威", "捷克共和国", "摩尔多瓦", "摩德纳足球俱乐部", "摩根州立大学熊队", "摩洛哥", "摩纳哥足球俱乐部", "文莱",
								"斋浦尔粉红豹队", "斯克兰顿/威尔克斯-巴里洋基队", "斯图加特足球俱乐部", "斯坦福大学深红队", "斯威士兰", "斯帕尔足球俱乐部",
								"斯托克城足球俱乐部", "斯托克顿港口队", "斯旺西城足球俱乐部", "斯普林菲尔德红雀队", "斯波坎印第安人队", "斯泰森大学制帽人队",
								"斯洛伐克", "斯洛文尼亚", "斯特凡·斯特鲁夫", "斯特拉斯堡足球俱乐部", "斯蒂普·米欧奇", "斯蒂芬·汤普森",
								"斯蒂芬奥斯汀州立大学伐木工人队", "斯里兰卡", "新不列颠石猫队", "新加坡", "新墨西哥大学罗伯斯队", "新墨西哥州立大学阿吉斯队",
								"新奥尔良圣徒队", "新奥尔良大学私掠船队", "新奥尔良鹈鹕队", "新泽西/纽约哥谭足球队", "新泽西州/纽约哥谭足球俱乐部",
								"新泽西理工学院高地人队", "新泽西篮网队", "新泽西魔鬼队", "新潟天鹅队", "新罕布什尔大学野猫队", "新罕布什尔捕鱼猫队",
								"新英格兰爱国者队", "新英格兰革命足球俱乐部", "新英格兰黑狼队", "新莱昂自治大学老虎足球俱乐部", "新西兰", "旁遮普足球俱乐部",
								"日本", "日本队", "旧金山49人队", "旧金山勇士队", "旧金山大学唐斯队", "旧金山巨人队", "昂热足球俱乐部",
								"昆尼匹克大学山猫队", "明尼苏达北极星队", "明尼苏达双城队", "明尼苏达大学金地鼠队", "明尼苏达山猫队", "明尼苏达森林狼队",
								"明尼苏达维京人队", "明尼苏达联", "明尼苏达荒野队", "普内里帕坦队", "普利茅斯足球俱乐部", "普拉斯基洋基队",
								"普林斯顿大学老虎队", "普林斯顿射线队", "普渡大学锅炉工队", "普罗维登斯学院修道士队", "普雷斯顿足球俱乐部", "智利",
								"曼哈顿学院贾斯珀队", "曼城足球俱乐部", "曼彻斯特联足球俱乐部", "朗伍德大学枪骑兵队", "朝阳海德拉巴队", "朝鲜", "木星锤头队",
								"本·阮", "本菲卡足球俱乐部", "札幌冈萨多足球队", "朱尼尔·多斯·桑托斯", "朱莉安娜·佩纳", "杜克大学蓝魔队",
								"杜兰大学绿浪队", "杜塞尔多夫足球俱乐部", "杨百翰大学美洲狮队", "杰克逊州立大学老虎队", "杰克逊维尔太阳队",
								"杰克逊维尔州立大学斗鸡队", "杰克逊维尔海豚队", "杰克逊维尔美洲虎队", "杰曼·德·兰达米", "杰皮旁遮普勇士队", "杰西卡·埃",
								"杰西卡·安德拉德", "杰西卡·罗斯·克拉克", "杰里米·斯蒂芬斯", "松本山雅足球俱乐部", "极限运动", "林奇堡山猫队",
								"果阿足球俱乐部", "柏太阳神足球队", "柏林欧绿保篮球俱乐部", "柏林赫塔足球俱乐部", "查塔努加观景峰队", "查尔斯顿南方海盗队",
								"查尔斯顿学院美洲狮队", "查尔斯顿河狗队", "查尔顿竞技足球俱乐部", "查德·门德斯", "柬埔寨", "柯蒂斯·布莱兹",
								"柳别尔齐胜利篮球俱乐部", "栃木足球俱乐部", "格兰布林州立大学老虎队", "格威内特勇士队", "格拉纳达足球俱乐部", "格林斯波罗蚱蜢队",
								"格林纳达", "格林维尔大道队", "格林维尔太空人队", "格洛弗·特谢拉", "格罗宁根足球俱乐部", "格鲁吉亚", "桑坦德竞技足球俱乐部",
								"桑德兰足球俱乐部", "桑托斯拉古纳俱乐部", "桑普多利亚足球俱乐部", "梅斯足球俱乐部", "梅赛德斯 AMG 马石油车队",
								"棕榈滩红雀队", "横滨 DeNA 海湾之星队", "横滨体育及文化会", "横滨水手队", "横滨足球俱乐部", "欧仁猫头鹰队",
								"欧力士野牛队", "欧塞尔足球俱乐部", "比利亚雷亚尔足球俱乐部", "比利时", "比勒费尔德足球俱乐部", "比林斯野马队", "比萨",
								"毕尔巴鄂竞技足球俱乐部", "毛里塔尼亚", "毛里求斯", "水户蜀葵足球队", "水晶宫足球俱乐部", "水牛城军刀", "水牛城勇士队",
								"水牛城强盗队", "水牛城野牛队", "汉堡足球俱乐部", "汉密尔顿老虎队", "汉密尔顿虎猫队", "汉普顿大学海盗队",
								"汉莎罗斯托克足球俱乐部", "汉诺威 96 足球俱乐部", "沃尔坎·厄兹德米尔", "沃尔夫斯堡足球俱乐部", "沃特福德足球俱乐部",
								"沃福德学院小猎狗队", "沙尔克 04 足球俱乐部", "沙特阿拉伯", "沙瓦那沙虫队", "沙米尔·阿卜杜拉希莫夫", "沙维什",
								"河床竞技俱乐部", "河床足球俱乐部", "法伦斯", "法兰克福足球俱乐部", "法国", "法拉利车队", "法马利康足球俱乐部", "波兰",
								"波塔基特红袜队", "波士顿凯尔特人队", "波士顿大学梗犬队", "波士顿学院鹰队", "波士顿棕熊队", "波士顿焗豆队", "波士顿红袜队",
								"波士顿食豆人队", "波多黎各", "波多黎各队", "波尔图足球俱乐部", "波尔多瓶", "波尔州立大学红雀队", "波托马克国民队",
								"波斯尼亚和黑塞哥维那", "波特兰伐木者足球俱乐部", "波特兰大学飞行员队", "波特兰州立大学维京人队", "波特兰开拓者队",
								"波特兰海狗队", "波特兰海狸队", "波特兰火焰队", "波特兰荆棘队", "波蒂莫嫩塞足球俱乐部", "波鸿足球俱乐部", "泰·特瓦萨",
								"泰伦·伍德利", "泰卢固泰坦队", "泰国", "泰尔纳纳", "泰森·佩德罗", "泰米尔塔莱瓦斯队", "泽维尔大学火枪手队",
								"泽西岛海岸蓝爪队", "洋杉激流麦子队", "洛威旋球队", "洛杉矶充电器", "洛杉矶公羊队", "洛杉矶国王队", "洛杉矶天使队",
								"洛杉矶快船队", "洛杉矶湖人队", "洛杉矶火花队", "洛杉矶足球俱乐部", "洛杉矶道奇队", "洛杉矶银河足球俱乐部", "洛杉矶闪电",
								"洛约拉马利蒙特大学雄狮队", "洛里昂足球俱乐部", "津巴布韦", "洪都拉斯", "流浪者足球俱乐部", "浦和红钻足球俱乐部",
								"浦那 7 王牌队", "海伦娜酿酒人队", "海伦芬体育俱乐部", "海军学院见习官队", "海地", "海得拉巴猎人队", "海得拉巴足球俱乐部",
								"海波因特大学黑豹队", "清水心跳足球队", "清水长尾鲛队", "渥太华参议员队", "渥太华竞技", "渥太华红黑队", "温哥华加人队",
								"温哥华加拿大人队", "温哥华勇士队", "温哥华白浪足球俱乐部", "温哥华足球俱乐部", "温尼伯喷射机队", "温尼伯荣军足球俱乐部",
								"温尼伯蓝色轰炸机足球队", "温斯罗普大学鹰队", "温斯顿塞勒姆冲刺队", "湘南丽海足球队", "澳大利亚", "火鸡肉",
								"热那亚足球俱乐部", "熊本深红足球俱乐部", "爱奥那学院盖尔斯队", "爱媛足球俱乐部", "爱尔兰", "爱沙尼亚", "爱荷华大学鹰眼队",
								"爱荷华小熊队", "爱荷华州立大学旋风队", "爱达荷大学破坏者队", "爱达荷州立大学孟加拉虎队", "爱达荷瀑布鹌鹑队", "牙买加",
								"特伦顿雷霆队", "特内里费足球俱乐部", "特拉华大学战斗蓝母鸡队", "特拉华州立大学黄蜂队", "特拉维夫马卡比篮球俱乐部",
								"特洛伊州立大学特洛伊人队", "特温特足球俱乐部", "特立尼达和多巴哥", "特西亚·托雷斯", "特鲁瓦足球俱乐部", "犹他大学犹特队",
								"犹他州立大学农人队", "犹他爵士队", "犹他皇家足球队", "犹他谷大学狼獾队", "独立奥林匹克运动员", "玛丽昂·雷诺",
								"玛拉·罗梅罗·博雷拉", "玻利维亚", "珂斯东孟加拉足球俱乐部", "班加罗尔公牛队", "班加罗尔猛龙队", "班加罗尔皇家挑战者俱乐部",
								"班加罗尔足球俱乐部", "理海谷野猪队", "琉球足球俱乐部", "瑞典", "瑞士", "瓜达拉哈拉竞技俱乐部", "瓦伦缇娜·舍甫琴科",
								"瓦伦西亚篮球俱乐部", "瓦伦西亚足球俱乐部", "瓦努阿图", "瓦尔帕莱索大学十字军队", "瓦尔韦克足球俱乐部", "瓦朗谢讷足球俱乐部",
								"瓦格纳学院海鹰队", "甘冈足球俱乐部", "甘纳·纳尔逊", "田纳西大学志愿者队", "田纳西大学查塔努加分校莫奇队",
								"田纳西大学科技金鹰队", "田纳西大学马丁分校天鹰队", "田纳西州立大学老虎队", "田纳西巨神队", "田纳西斯摩基队", "田纳西泰坦队",
								"甲府风林足球俱乐部", "电子竞技", "町田泽维亚足球俱乐部", "白俄罗斯", "白求恩库克曼大学野猫队", "百年绅士队", "百慕大",
								"皇家克什米尔足球俱乐部", "皇家奥维耶多足球俱乐部", "皇家盐湖城队", "皇家社会足球俱乐部", "皇家萨拉戈萨足球俱乐部", "皇家马德里",
								"皇家马德里篮球队", "皮亚琴察足球俱乐部", "皮奥里亚酋长队", "盐湖城蜜蜂队", "相模原体育俱乐部", "石溪大学海狼队",
								"磐城足球俱乐部", "磐田喜悦足球队", "神户胜利船队", "福冈软银鹰队", "福冈黄蜂足球俱乐部", "福岛联足球俱乐部",
								"福特汉姆大学公羊队", "秋田蓝闪电足球俱乐部", "科尔比·科文顿", "科尔盖特大学突袭者队", "科平州立老鹰队", "科摩罗",
								"科林蒂安保利斯塔体育会", "科森察", "科特尼·凯西", "科特布斯足球俱乐部", "科特迪瓦", "科珀斯克里斯蒂铁钩队", "科索沃",
								"科罗拉多大学老虎队", "科罗拉多州立大学公羊队", "科罗拉多急流队", "科罗拉多斯普林斯天空袜队", "科罗拉多水牛队",
								"科罗拉多洛矶山脉", "科罗拉多猛犸队", "科罗拉多雪崩队", "科莫足球俱乐部", "科迪·加布兰特", "科迪·斯塔曼", "科里·安德森",
								"秘鲁", "空军猎鹰队", "突尼斯", "立普斯康大学野牛队", "立陶宛", "第戎足球俱乐部", "米兰奥林匹亚篮球俱乐部",
								"米兰德斯足球俱乐部", "米尔沃尔足球俱乐部", "米尔萨德·贝克蒂奇", "米德兰摇滚猎犬队", "米德尔斯堡足球俱乐部", "米歇尔·沃特森",
								"米沙·齐尔库诺夫", "索肖足球俱乐部", "索马里", "约尔·罗梅罗", "约旦", "约瑟夫·贝纳维德兹", "约翰·多德森",
								"约翰·莫拉加", "约翰·莱因克尔", "约翰逊城红衣主教队", "纳什维尔天籁队", "纳什维尔掠夺者队", "纳什维尔足球俱乐部",
								"纳米比亚", "纽伦堡足球俱乐部", "纽卡斯尔联足球俱乐部", "纽约FC", "纽约喷气机队", "纽约大学山猫队", "纽约大学紫罗兰队",
								"纽约大都会队", "纽约尼克斯队", "纽约岛民队", "纽约巨人队", "纽约洋基队", "纽约游骑兵队", "纽约激流队",
								"纽约理工学院熊队", "纽约红牛队", "纽约美国人队", "纽约自由人队", "纽约高地人队", "维加斯黄金骑士队", "维尔京群岛",
								"维戈塞尔塔足球俱乐部", "维拉诺瓦野猫队", "维特斯职业足球基金会", "维罗纳足球俱乐部", "维萨利亚拉希德队", "绿湾包装工队",
								"缅因大学黑熊队", "缅甸", "网球", "罗伯特·惠特克", "罗伯特莫里斯殖民地队", "罗克珊·莫达菲里", "罗切斯特皇家队",
								"罗切斯特红翼队", "罗切斯特骑士鹰队", "罗布·冯特", "罗德岛大学公羊队", "罗斯·娜玛朱纳斯", "罗格斯红衣骑士队", "罗比·劳勒",
								"罗瑟勒姆足球俱乐部", "罗达 JC 足球俱乐部", "罗马体育俱乐部", "罗马勇士队", "罗马尼亚", "美因茨足球俱乐部", "美国",
								"美国大学老鹰队", "美国队", "美属萨摩亚", "美洲足球俱乐部", "群马草津温泉队", "老挝", "老道明大学君主队",
								"考文垂足球俱乐部", "耶鲁大学斗牛犬队", "肯塔基大学野猫队", "肯尼亚", "肯尼索州立猫头鹰队", "肯特州立大学金色光芒队",
								"艾佛列特绿袜队", "艾克隆航空队", "艾哈迈达巴德后卫队", "艾查威尔足球俱乐部", "艾森豪湖风暴队", "艾法托利车队",
								"艾琳·阿尔达娜", "艾维斯体育俱乐部", "芝加哥公牛队", "芝加哥天空队", "芝加哥小熊队", "芝加哥州立美洲狮队",
								"芝加哥洛约拉大学漫步者队", "芝加哥火焰队", "芝加哥熊队", "芝加哥牡鹿队", "芝加哥白袜队", "芝加哥白长袜队", "芝加哥红星队",
								"芝加哥西风队", "芝加哥黑鹰队", "芬兰", "苏丹", "苏格兰", "苏里南", "英国", "英属维尔京群岛", "范德比尔特大学船长队",
								"草原风光农工大学美洲豹队", "荷兰", "荷兰队", "莫亨巴根足球俱乐部", "莫尔黑德州立大学老鹰队", "莫斯科中央陆军篮球俱乐部",
								"莫斯科斯巴达克足球俱乐部", "莫桑比克", "莫比尔海湾熊队", "莫雷伦斯足球俱乐部", "莱万特足球俱乐部", "莱克兰飞虎队",
								"莱克郡队长队", "莱切", "莱加内斯足球俱乐部", "莱德大学野马队", "莱斯大学猫头鹰队", "莱斯特城足球俱乐部",
								"莱比锡红牛足球俱乐部", "莱特州立大学突袭者队", "莱科", "莱索托", "菲利斯·赫里格", "菲尔莱狄更斯大学魔鬼队",
								"菲尼克斯太阳队", "菲尼克斯水星队", "菲律宾", "萨克拉门托君主队", "萨克拉门托国王队", "萨克拉门托河猫队",
								"萨兰迪兵工厂足球俱乐部", "萨凡纳州立大学老虎队", "萨勒尼塔纳足球俱乐部", "萨姆休斯顿州立大学熊狸队", "萨姆福德大学斗牛犬队",
								"萨尔瓦多", "萨拉·麦克曼", "萨拉基利斯篮球俱乐部", "萨摩亚", "萨斯喀彻温热潮队", "萨斯喀彻温省骑兵队", "萨索罗足球俱乐部",
								"葡萄牙", "蒂丁路运动联合会足球俱乐部", "蒂亚戈·桑托斯", "蒂姆·埃利奥特", "蒂尔堡威廉二世足球俱乐部", "蒙古",
								"蒙哥马利饼干队", "蒙大拿州立大学山猫队", "蒙大拿灰熊队", "蒙彼利埃足球俱乐部", "蒙扎", "蒙特利尔云雀队", "蒙特利尔冲击",
								"蒙特利尔加拿大人队", "蒙特利尔博览会队", "蒙特利尔流浪者队", "蒙特利尔褐红队", "蒙特雷足球俱乐部", "蒙茅斯大学老鹰队",
								"蓝十字竞技俱乐部", "蓝田金莺队", "藤枝 MYFC 足球俱乐部", "西伊利诺伊大学海军陆战队员队", "西北大学野猫队",
								"西北州立大学恶魔队", "西北阿肯色自然队", "西南密苏里州立大学熊队", "西卡罗莱纳大学大山猫队", "西印度群岛", "西密歇根大学野马队",
								"西密歇根白帽队", "西布罗姆维奇足球俱乐部", "西弗吉尼亚力量队", "西弗吉尼亚大学登山人队", "西拉丘斯国民队", "西汉姆联足球俱乐部",
								"西班牙", "西班牙人足球俱乐部", "西田纳西钻石工队", "西肯塔基大学山顶队", "西贾拉·尤班克斯", "西雅图大学红鹰队",
								"西雅图水手队", "西雅图海妖队", "西雅图海湾人足球俱乐部", "西雅图海鹰队", "西雅图超音速队", "西雅图风暴队",
								"要塞军事学院斗牛犬队", "詹妮弗·迈亚", "詹姆斯·维克", "詹姆斯敦干扰者队", "詹姆斯麦迪逊大学公爵队", "詹谢普尔足球俱乐部",
								"诺丁汉森林足球俱乐部", "诺福克州立大学斯巴达人队", "诺福克潮汐队", "读卖巨人队", "谢菲尔德星期三足球俱乐部",
								"谢菲尔德联足球俱乐部", "象牙海岸", "豪尔赫·马斯维达尔", "贝伦人足球俱乐部", "贝克斯菲尔德火焰队", "贝勒大学熊队", "贝宁",
								"贝尔格莱德红星篮球俱乐部", "贝尔蒙特大学棕熊队", "贝洛伊特鲷鱼队", "贝瑟·科雷娅", "贝茨山猫队", "贝蒂斯塞维利亚足球俱乐部",
								"费内巴切伊斯坦布尔篮球俱乐部", "费城76人队", "费城之翼队", "费城老鹰队", "费城联足球俱乐部", "费城贵格会队",
								"费城费城人队", "费城运动家队", "费城飞人队", "费尔利狄金森大学骑士队", "费尔菲尔德大学雄鹿队", "费耶诺德鹿特丹足球俱乐部",
								"费雷拉", "费雷拉柏苏斯足球俱乐部", "贾卡雷·苏扎", "贾斯汀·加瑟基", "贾斯汀·威利斯", "贾里德·坎诺尼尔",
								"赞岐釜玉海足球队", "赞比亚", "赤道几内亚", "赫塔菲足球俱乐部", "赫尔城足球俱乐部", "赫拉克勒斯足球俱乐部",
								"赫罗纳足球俱乐部", "越南", "足球俱乐部东京", "路易斯安那东南狮队", "路易斯安那大学拉法叶拉金卡津队",
								"路易斯安那大学门罗分校战鹰队", "路易斯安那州立大学老虎队", "路易斯安那州门罗大学印第安人队", "路易斯安那理工大学牛头犬队",
								"路易斯维尔大学红雀队", "路易斯维尔蝙蝠队", "路易斯维尔赛车队", "辛辛那提大学熊狸队", "辛辛那提猛虎队", "辛辛那提红人队",
								"辛辛那提红腿队", "辛辛那提足球俱乐部", "达伦·埃尔金斯", "达伦·蒂尔", "达勒姆公牛队", "达拉斯德州人队", "达拉斯星队",
								"达拉斯浸会大学爱国者队", "达拉斯牛仔队", "达拉斯独行侠队", "达拉斯足球俱乐部", "达拉斯飞翼队", "达斯汀·奥尔蒂斯",
								"达斯汀·波里耶", "达特茅斯大绿队", "迈克尔·基耶萨", "迈尔斯堡奇迹队", "迈阿密国际足球俱乐部", "迈阿密大学飓风队",
								"迈阿密大学（俄亥俄州）红鹰队", "迈阿密太阳神队", "迈阿密海豚队", "迈阿密热火队", "迈阿密马林鱼队", "进击的浦那超级巨人队",
								"迪加史卓普足球队", "迪尤肯大学公爵队", "通德拉足球俱乐部", "道格拉斯·席尔瓦·德·安德拉德", "邓弗姆林竞技足球俱乐部",
								"邓迪联足球俱乐部", "邓迪足球俱乐部", "那不勒斯足球俱乐部", "郑赞盛", "都灵", "里卡多·拉马斯", "里士满大学蜘蛛队",
								"里士满飞鼠队", "里奥艾维足球俱乐部", "里尔足球俱乐部", "里昂·爱德华兹", "里昂足球俱乐部", "金士顿印第安人队",
								"金奈银足球俱乐部", "金州勇士队", "金斯波特大都会队", "金泽塞维根足球俱乐部", "金翼鹦鹉", "钦奈城足球俱乐部",
								"钦奈斯巴达人队", "钦奈超级国王队", "钦奈超级星队", "锡耶纳学院圣徒队", "镜片", "长岛大学黑鸟队", "长崎成功丸足球俱乐部",
								"长滩州立大学 49 人队", "长滩州立大学鲨鱼队", "长老会学院蓝色软管队", "长野帕塞罗体育俱乐部", "门兴格拉德巴赫足球俱乐部",
								"阅读", "阪神虎队", "阿什维尔旅行者队", "阿什莉·埃文斯-史密斯", "阿伯丁足球俱乐部", "阿伯丁铁鸟队", "阿克伦大学拉链靴队",
								"阿利斯泰尔·奥弗瑞姆", "阿塞拜疆", "阿富汗", "阿尔·亚昆塔", "阿尔伯克基同位素队", "阿尔克马尔足球俱乐部", "阿尔及利亚",
								"阿尔图纳曲线队", "阿尔巴塞特足球俱乐部", "阿尔巴尼亚", "阿尔梅里亚足球俱乐部", "阿尔法罗密欧车队", "阿尔科孔足球俱乐部",
								"阿巴拉契亚州登山者队", "阿拉伯联合酋长国", "阿拉巴马农工大学斗牛犬队", "阿拉巴马大学伯明翰分校开拓者队", "阿拉巴马州立大学黄蜂队",
								"阿拉巴马赤潮队", "阿拉维斯足球俱乐部", "阿斯彭·莱德", "阿斯科利", "阿斯顿维拉足球俱乐部", "阿曼", "阿曼达·努内斯",
								"阿根廷", "阿森纳足球俱乐部", "阿比林基督教大学野猫队", "阿特拉斯足球俱乐部", "阿瓦德勇士队", "阿罗卡",
								"阿肯色大学松树壁分校金狮队", "阿肯色大学野猪队", "阿肯色州中部熊队", "阿肯色州小石城特洛伊队", "阿肯色州立大学印地安人队",
								"阿肯色州立大学红狼队", "阿肯色旅行者队", "阿贾克斯足球俱乐部", "阿贾曼·斯特林", "阿雅克肖足球俱乐部", "阿鲁巴岛",
								"陆军黑骑士队", "陶森大学老虎队", "难民奥运队", "雅吉玛熊队", "雪城大学橙人队", "雪城酋长队", "雷·博格", "雷丁费城人队",
								"雷吉纳足球俱乐部", "雷恩足球俱乐部", "雷纳托·莫伊卡诺", "雷诺 DP World 车队", "雷诺王牌队", "霍华德大学野牛队",
								"霍夫斯特拉大学骄傲队", "霍夫斯特拉飞行荷兰人队", "霍芬海姆足球俱乐部", "霍莉·霍尔姆", "露西·普迪洛娃", "韦伯州立野猫队",
								"韦尔瓦足球俱乐部", "韦恩堡乳齿象队", "韦恩堡活塞队", "韦恩堡锡帽队", "韦斯卡足球俱乐部", "韩国", "韩国队", "香港",
								"马修斯·尼科劳", "马克·亨特", "马克斯·霍洛威", "马其顿", "马奎特大学金鹰队", "马尔代夫", "马德里竞技足球俱乐部",
								"马拉加足球俱乐部", "马拉维", "马来西亚", "马歇尔大学雷霆追风队", "马瑟韦尔足球俱乐部", "马略卡足球俱乐部", "马绍尔群岛",
								"马耳他", "马萨诸塞大学民兵队", "马赛足球俱乐部", "马辛·泰布拉", "马达加斯加", "马里", "马里兰大学东岸分校战鹰队",
								"马里兰大学巴尔的摩县猎犬队", "马里兰大学水龟队", "马里兰洛约拉大学灰狗队", "马里斯特学院红狐狸队", "马里迪莫体育俱乐部",
								"马霍宁谷拳击手队", "马龙·莫拉斯", "高中棒球队", "高山", "高沙漠小牛队", "高知蓝强攻队", "鲍伊贝索克斯队",
								"鲍灵格林热棒队", "鲍灵格林猎鹰队", "鸟取飞翔足球俱乐部", "鸟栖砂岩足球队", "鹿岛鹿角足球俱乐部", "鹿特丹精英足球俱乐部",
								"麦克尼斯州立大学牛仔队", "麦肯齐·邓恩", "麻省大学罗威尔分校河鹰队", "黎巴嫩", "黑山", "黑格斯敦太阳队", "黑鹰海得拉巴队",
								"默特尔比奇鹈鹕队", "默瑟大学熊队", "默里州立大学赛车手队"
							],
							"enumNames": ["1.科隆足球俱乐部", "1_fc_magdeburg", "A. J. Allmendinger", "A.S.利沃诺足球俱乐部",
								"AC 米兰足球俱乐部", "ADO 海牙足球俱乐部", "airdrieonians", "AISIN AW AREIONS ANJO",
								"AKITA NORTHERN HAPPINETS", "Alex Bowman", "almere_city", "ALVARK TOKYO",
								"AOMORI WAT'S", "arbroath", "Aric Almirola", "Aston Martin", "ASVEL篮球队",
								"ATK 莫亨巴根足球俱乐部", "atletico_de_san_luis", "Austin Dillon", "BAMBITIOUS NARA",
								"Bobby Labonte", "Brad Keselowski", "BWT 赛点车队", "C.D.美国芝华士足球俱乐部",
								"Carl Edwards", "Casey Mears", "Chase Elliott", "CHIBA JETS",
								"Chris Buescher", "clermont_foot_63", "Clint Bowyer", "Dale Earnhardt",
								"Dale Earnhardt Jr.", "Daniel Suárez", "darmstadt_98",
								"Darrell Wallace Jr.", "David Ragan", "DC联队", "Denny Hamlin",
								"EARTHFRIENDS TOKYO Z", "EHIME ORANGE VIKINGS", "Elliott Sadler",
								"Erik Jones", "F EAGLES NAGOYA", "F.C.岐阜足球俱乐部", "fc_heidenheim",
								"fc_juarez", "fc_volendam", "Fiji", "fortuna_sittard",
								"FUKUSHIMA FIREBONDS", "Gray Gaulding", "Greg Biffle", "greuther_furth",
								"GUNMA CRANE THUNDERS", "HIROSHIMA DRAGONFLIES", "IBARAKI ROBOTS",
								"inverness_caledonian_thistle", "IWATE BIG BULLS", "Jamie McMurray",
								"Jeff Burton", "Jeff Gordon", "Jimmie Johnson", "Joey Logano",
								"Juan Pablo Montoya", "KAGAWA FIVE ARROWS", "KAGOSHIMA REBNISE",
								"KANAZAWA SAMURAIZ", "karlsruher_sc", "Kasey Kahne",
								"KAWASAKI BRAVE THUNDERS", "Kevin Harvick", "KUMAMOTO VOLTERS",
								"Kurt Busch", "Kyle Busch", "Kyle Larson", "KYOTO HANNARYZ",
								"LEVANGA HOKKAIDO", "LPGA", "Mark Martin", "Martin Truex Jr.",
								"Matt DiBenedetto", "Matt Kenseth", "mazatlan", "Mclaren",
								"Michael McDowell", "NAC 布雷达足球俱乐部", "NAGOYA DIAMOND DOLPHINS", "Nauru",
								"NCR 旁遮普皇家队", "NEC 奈梅亨足球俱乐部", "necaxa", "NEROCA FC", "NIIGATA ALBIREX BB",
								"NISHINOMIYA STORKS", "norwich", "OSAKA EVESSA",
								"OTSUKA CORPORATION ALPHAS", "Paul Menard", "PGA", "PSG", "PSV 埃因霍温足球俱乐部",
								"QPR", "queretaro", "RBC 罗辛达足球俱乐部", "Red Bull Racing", "Richard Petty",
								"Ricky Stenhouse Jr.", "RIZING ZEPHYR FUKUOKA", "Roma", "ross_county",
								"Ryan Blaney", "Ryan Newman", "RYUKYU GOLDEN KINGS", "SAITAMA BRONCOS",
								"SAN-EN NEOPHOENIX", "SEAHORSES MIKAWA", "SENDAI EIGHTY NINERS",
								"SHIGA LAKESTARS", "SHIMANE SUSANOO MAGIC", "SHINSHU BRAVE WARRIORS",
								"sparta_rotterdam", "st_johnstone", "st_mirren", "SUNROCKERS SHIBUYA",
								"TJ·迪拉肖", "TOCHIGI BREX", "TOKIO MARINE NICHIDO BIG BLUE",
								"TOKYO CINQ REVES", "TOKYO EXCELLENCE", "TOKYO HACHIOJI TRAINS", "Tonga",
								"Tony Stewart", "TOYAMA GROUSES", "TOYODA GOSEI SCORPIONS", "Trevor Bayne",
								"Tuvalu", "Ty Dillon", "union_berlin", "UP 丹加勒队", "vfl_osnabrück",
								"William Byron", "WUSA", "WWE", "YAMAGATA WYVERNS", "YOKOHAMA B-CORSAIRS",
								"York9 FC", "三城谷猫队", "三市尘埃魔鬼队", "不丹", "不列颠哥伦比亚雄狮队", "丘吉尔兄弟足球俱乐部",
								"东京养乐多燕子队", "东京绿茵足球俱乐部", "东伊利诺伊大学黑豹队", "东北乐天金鹫队", "东北伊利诺伊州金鹰队", "东北勇士队",
								"东北大学哈士奇队", "东北联足球俱乐部", "东华盛顿大学老鹰队", "东南密苏里州印第安人队", "东南密苏里州州立大学红鹰队",
								"东卡罗来纳大学海盗队", "东密歇根大学老鹰队", "东帝汶", "东田纳西州州立大学海盗队", "东肯塔基大学上校队",
								"中佛罗里达大学金骑士队", "中华台北", "中国", "中密歇根大学奇普瓦斯队", "中康涅狄格大学蓝魔队", "中日龙队",
								"中田纳西州立大学蓝色突袭者队", "中量级", "中非共和国", "丹·胡克", "丹佛大学先驱者队", "丹佛掘金队", "丹佛野马队",
								"丹尼尔·科米尔", "丹维尔勇士队", "丹麦", "丽兹·卡穆什", "丽娜·兰斯伯格", "乌克兰", "乌兹别克斯坦", "乌利亚·霍尔",
								"乌姆巴排球对", "乌姆巴队", "乌干达", "乌德勒支足球俱乐部", "乌拉圭", "乌迪内斯足球俱乐部", "乍得", "乔什·埃米特",
								"乔安妮·考尔德伍德", "乔安娜·耶德尔泽奇克", "乔恩·琼斯", "乔治·圣皮埃尔", "乔治亚南方大学鹰队", "乔治亚斗牛犬队",
								"乔治亚理工大黄蜂队", "乔治亚蜂群队", "乔治华盛顿殖民者队", "乔治城大学惊叹队", "乔治梅森大学爱国者队", "也门",
								"云达不莱梅足球俱乐部", "亚利桑那响尾蛇队", "亚利桑那大学野猫队", "亚利桑那州立大学太阳魔队", "亚利桑那红雀队", "亚利桑那郊狼队",
								"亚历克斯·佩雷斯", "亚历克斯·奥利维拉", "亚历克萨·格拉索", "亚历克西斯·戴维斯", "亚历山大·古斯塔夫森",
								"亚历山大·埃尔南德斯", "亚历山大·沃尔卡诺斯基", "亚历山大·沃尔科夫", "亚历山大·潘托哈", "亚历杭德罗·佩雷斯",
								"亚娜·库尼茨卡娅", "亚尔·罗德里格斯", "亚特兰大勇士队", "亚特兰大梦想队", "亚特兰大火焰队", "亚特兰大猎鹰队",
								"亚特兰大老鹰队", "亚特兰大联足球俱乐部", "亚特兰大足球俱乐部", "亚特兰大鸫鸟队", "亚眠竞技俱乐部", "亚美尼亚",
								"亨利·塞胡多", "亨茨维尔星队", "京都不死鸟足球俱乐部", "仙台七夕足球队", "代托纳小熊队", "以色列", "伊利尔·拉提菲",
								"伊利海狼队", "伊利莎白顿双城队", "伊利诺伊大学伊利尼队", "伊利诺伊大学芝加哥分校火焰队", "伊利诺伊州立大学红雀队", "伊拉克",
								"伊斯雷尔·阿德桑亚", "伊普斯维奇城足球俱乐部", "伊朗", "伊隆大学凤凰队", "伍尔弗汉普顿流浪者足球俱乐部", "休斯顿大学美洲狮队",
								"休斯顿太空人队", "休斯顿彗星队", "休斯顿德州人队", "休斯顿浸会大学哈士奇队", "休斯顿火箭队", "休斯顿达斯足球俱乐部",
								"休斯顿迪纳摩队", "伦秋库卡蒙佳地震队", "伯利兹", "伯恩利足球俱乐部", "伯恩茅斯足球俱乐部", "伯明翰城足球俱乐部",
								"伯明翰男爵队", "伯灵顿皇家队", "伯灵顿蜜蜂队", "但尼丁蓝鸟队", "佐佐木佑太", "佐治亚州立大学黑豹队", "体育足球俱乐部",
								"何塞·奥尔多", "佛得角", "佛罗伦萨足球俱乐部", "佛罗里达农工大学响尾蛇队", "佛罗里达国际大学金豹队", "佛罗里达国际大学黑豹队",
								"佛罗里达大学短吻鳄队", "佛罗里达大西洋猫头鹰队", "佛罗里达州立大学塞米诺尔队", "佛罗里达海湾海岸鹰队", "佛罗里达美洲豹队",
								"佛蒙特大山猫队", "佛蒙特湖怪兽队", "佩德罗·蒙霍兹", "佩珀代因大学波浪队", "佩鲁贾足球俱乐部", "依云足球俱乐部",
								"俄亥俄大学山猫队", "俄亥俄州立大学七叶树队", "俄克拉荷马城红鹰队", "俄克拉荷马大学捷足者队", "俄克拉荷马州立大学牛仔队",
								"俄克拉荷马雷霆队", "俄勒冈大学鸭子队", "俄勒冈州立大学海狸队", "俄罗斯", "保加利亚", "保罗·科斯塔", "保罗·费尔德",
								"克利夫兰州立维京人队", "克利夫兰布朗队", "克利夫兰男爵队", "克利夫兰纳普斯队", "克利夫兰蓝队", "克利夫兰骑士队",
								"克劳迪娅·加德哈", "克林顿伐木王队", "克罗地亚", "克莱姆森大学老虎队", "克赖顿大学蓝鸟队", "克里夫兰印地安人队",
								"克里夫兰摇滚者队", "克里斯·赛博格", "克里斯·魏德曼", "兰契雷曲棍球队", "兰开斯特喷射鹰队", "兰斯足球俱乐部", "兰辛螺帽队",
								"兰达·马科斯", "关岛", "兹沃勒足球俱乐部", "内华达大学拉斯维加斯分校叛逆者队", "内华达大学狼群队", "内布拉斯加大学剥玉米人队",
								"内特·迪亚兹", "内陆帝国 66 人队", "冈山绿雉足球俱乐部", "冈比亚", "冈萨加大学斗牛犬队", "冰岛", "几内亚",
								"几内亚比绍", "凯伦·维埃拉", "凯尔特人足球俱乐部", "凯恩县美洲狮队", "凯文·李", "凯文·盖斯特鲁姆", "凯泽斯劳滕足球俱乐部",
								"凯特·辛加诺", "凯特琳·乔卡吉安", "切尔西靴", "切沃维罗纳足球俱乐部", "列克星敦传奇队", "列支敦士登", "刚果",
								"刚果民主共和国", "利伯缇大学火焰队", "利兹联足球俱乐部", "利哈伊山大学老鹰队", "利文斯顿足球俱乐部", "利比亚", "利比里亚",
								"利物浦", "前进之鹰足球俱乐部", "加尔各答骑士团队", "加州大学圣巴巴拉分校高乔人队", "加州大学尔湾分校食蚁兽队",
								"加州大学戴维斯分校队", "加州大学河滨高地人队", "加州大学洛杉矶分校棕熊队", "加州天使队", "加州州立大学北岭斗牛士队",
								"加州州立大学富勒顿泰坦队", "加州州立大学萨克拉门托黄蜂队", "加州州立大学贝克斯菲尔德路跑者队", "加州海豹队", "加州浸会大学兰瑟斯队",
								"加州理工大学野马队", "加州金熊队", "加德纳韦伯鲁宁斗牛犬队", "加拉塔萨雷足球俱乐部", "加拿大", "加的斯足球俱乐部", "加纳",
								"加蓬", "努曼西亚足球俱乐部", "劳伦·墨菲增加 1", "勒阿弗尔足球俱乐部", "匈牙利", "北九州向日葵队",
								"北亚利桑那大学伐木工人队", "北伊利诺斯大学哈士奇队", "北佛罗里达大学鱼鹰队", "北卡罗来纳中央老鹰队", "北卡罗来纳农工大学队",
								"北卡罗来纳勇气队", "北卡罗来纳大学夏洛特分校 49 人队", "北卡罗来纳大学威尔明顿分校海鹰队", "北卡罗来纳大学格林斯伯勒分校斯巴达人队",
								"北卡罗来纳州立大学狼群队", "北卡罗来纳州阿什维尔斗牛犬队", "北卡罗莱纳大学焦油踵队", "北德克萨斯大学蛮横格林队", "北方邦奇才队",
								"北海道日本火腿斗士队", "北爱荷华大学黑豹队", "北科罗拉多熊队", "北肯塔基大学挪威人队", "北达科他大学战斗鹰队",
								"北达科他大学斗鹰队", "北达科他大学队", "北达科他州立大学野牛队", "北阿拉巴马狮子队", "匹兹堡企鹅队", "匹兹堡大学黑豹队",
								"匹兹堡海盗队", "匹兹堡钢人队", "千叶罗德海洋队", "华盛顿国民队", "华盛顿大学哈士奇队", "华盛顿奇才队",
								"华盛顿州立大学美洲狮队", "华盛顿指挥官", "华盛顿神秘人队", "华盛顿精神足球俱乐部", "华盛顿美式足球队", "华盛顿首都队",
								"南伊利诺伊大学萨路基猎犬队", "南伊利诺大学爱德华城美洲狮队", "南佛罗里达大学公牛队", "南加州大学北部分校斯巴达人队",
								"南加州大学特洛伊人队", "南卡罗来纳州大学斗鸡队", "南卡罗来纳州立大学斗牛犬队", "南卫理工会大学野马队", "南安普敦足球俱乐部",
								"南密西西比大学金鹰队", "南弯银鹰队", "南斯拉夫", "南方大学美洲虎队", "南特足球俱乐部", "南犹他大学雷鸟队", "南苏丹",
								"南达科他大学郊狼队", "南达科他州立大学野兔队", "南阿拉巴马大学美洲虎队", "南非", "博伊西州立大学野马队", "博伊西老鹰队",
								"博卡青年队", "博尔顿足球俱乐部", "博洛尼亚足球俱乐部", "博茨瓦纳", "博阿维斯塔足球俱乐部", "卡利亚里足球俱乐部",
								"卡利卡特英雄队", "卡哈比·努尔马格妙多夫", "卡坦扎罗", "卡塔尔", "卡塔尼亚足球俱乐部", "卡尔加里火焰队", "卡尔加里牛仔队",
								"卡尔加里罗内克斯队", "卡尔加里骑兵足球俱乐部", "卡尼修斯学院金狮鹫兽队", "卡布·斯旺森", "卡拉·埃斯帕萨", "卡斯柏小鬼队",
								"卡昂足球俱乐部", "卡林加枪骑兵队", "卡罗琳娜·科沃凯维奇", "卡罗莱纳泥鳅队", "卡罗莱纳海岸大学唱诗班队", "卡罗莱纳飓风队",
								"卡罗莱纳黑豹队", "卡萨皮亚", "卡迪夫城足球俱乐部", "卡马鲁·乌斯曼", "卢克·洛克霍尔德", "卢戈足球俱乐部", "卢旺达",
								"卢森堡", "卢顿足球俱乐部", "印度尼西亚", "印度音乐", "印第安纳大学山地人队", "印第安纳大学美洲虎队",
								"印第安纳大学－普渡大学韦恩堡分校乳齿象队", "印第安纳州梧桐队", "印第安纳步行者队", "印第安纳波利斯印地安人队",
								"印第安纳波利斯小马队", "印第安纳狂热队", "印第安阿罗足球俱乐部", "危地马拉", "厄瓜多尔", "厄立特里亚", "叙利亚",
								"古吉拉特狮队", "古吉拉特邦巨星队", "古巴", "古巴队", "古库拉姆喀拉拉邦足球俱乐部", "史坦顿岛洋基队", "吉尔吉斯斯坦",
								"吉尔维森特足球俱乐部", "吉布提", "吉米·里维拉", "吉米·马努瓦", "吉马良斯维多利亚足球俱乐部", "名古屋鲸鱼队",
								"哈佛大学深红队", "哈利法克斯流浪者足球俱乐部", "哈利法克斯雷鸟队", "哈密尔顿锻钢足球俱乐部", "哈德斯菲尔德足球俱乐部",
								"哈德逊河谷叛徒队", "哈斯 F1 车队", "哈特福德大学老鹰队", "哈特福德捕鲸人队", "哈萨克斯坦", "哈里亚纳邦铁锤队",
								"哈里亚纳钢人队", "哈里斯堡参议员队", "哥伦布快船队", "哥伦布机员足球俱乐部", "哥伦布蓝衣队", "哥伦比亚",
								"哥伦比亚大学狮子队", "哥斯达黎加", "唐纳德·塞罗内", "喀拉拉爆破手足球俱乐部", "喀麦隆", "国王十一旁遮普队",
								"国立自治大学俱乐部- 美洲狮", "国际米兰", "图卢兹足球俱乐部", "图拉兵工厂足球俱乐部", "圆石城特快车队", "土库曼斯坦",
								"圣何塞地震队", "圣何塞州立大学斯巴达人队", "圣何塞巨人队", "圣何塞鲨鱼队", "圣保利足球俱乐部", "圣保罗足球俱乐部",
								"圣克拉拉足球俱乐部", "圣克拉拉野马队", "圣十字学院十字军队", "圣博纳旺蒂尔邦尼队", "圣卢西亚", "圣地亚哥·庞齐尼比奥",
								"圣地亚哥大学斗牛士队", "圣地亚哥州立大学阿兹台克人队", "圣地亚哥快船队", "圣地亚哥教士队", "圣地亚哥波浪足球俱乐部",
								"圣地亚哥海豹队", "圣地亚哥火箭队", "圣埃蒂安足球俱乐部", "圣基茨和尼维斯", "圣多美和普林西比", "圣安东尼奥使命队",
								"圣安东尼奥银星队", "圣安东尼奥马刺队", "圣弗朗西斯大学（宾夕法尼亚州）红色闪光队", "圣弗朗西斯学院（纽约）梗犬队",
								"圣弗朗西斯布鲁克林梗队", "圣彼得大学孔雀队", "圣心先锋队", "圣文森特和格林纳丁斯", "圣母大学爱尔兰战士队", "圣玛丽大学盖尔人队",
								"圣玛丽学院响尾蛇队", "圣玛丽山大学登山者队", "圣约瑟夫老鹰队", "圣约翰红色风暴队", "圣路易斯城", "圣路易斯大学比利肯斯队",
								"圣路易斯红雀队", "圣路易斯老鹰队", "圣路易斯蓝调队", "圣路易斯轰炸机队", "圣道学院红雀队", "圣露西大都会队", "圣马力诺",
								"圭亚那", "坎布尔足球俱乐部", "坎纳波利斯大炮队", "坎贝尔大学战斗骆驼队", "坦帕洋基队", "坦帕湾光芒队", "坦帕湾海盗队",
								"坦帕湾闪电队", "坦桑尼亚", "埃利亚斯·西奥多鲁", "埃利泽·扎莱斯基·多斯桑托斯", "埃及", "埃塞俄比亚", "埃尔切足球俱乐部",
								"埃弗顿足球俱乐部", "埃德森·巴博萨", "埃德蒙顿油人队", "埃德蒙顿足球俱乐部", "埃德蒙顿足球队", "埃文斯维尔紫色王牌队",
								"埃斯托里尔", "埃斯特雷马杜拉足球俱乐部", "埃瓦尔足球俱乐部", "基尔马诺克足球俱乐部", "基里巴斯", "埼玉西武狮队",
								"堪萨斯城体育会队", "堪萨斯城国王队", "堪萨斯城潮流", "堪萨斯城田径队", "堪萨斯城皇家队", "堪萨斯城童子军队",
								"堪萨斯城酋长队", "堪萨斯大学杰鹰队", "堪萨斯州立大学野猫队", "塔可马雨人队", "塔吉克斯坦", "塔尔萨大学金色飓风队",
								"塔尔萨钻探者队", "塔尔萨震动队", "塔蒂亚娜·苏亚雷斯", "塞勒姆凯泽火山队", "塞图巴尔维多利亚足球俱乐部", "塞尔吉奥·佩蒂斯",
								"塞尔维亚", "塞尔维亚和黑山", "塞拉利昂", "塞浦路斯", "塞维利亚足球俱乐部", "塞舌尔", "塞顿霍尔大学海盗队", "墨西哥",
								"墨里西欧·鲁阿", "夏威夷大学彩虹勇士队", "夏洛特大学 49 人队", "夏洛特石蟹队", "夏洛特针刺队", "夏洛特骑士队",
								"夏洛特黄蜂队", "多伦多圣帕茨队", "多伦多摇滚队", "多伦多枫叶队", "多伦多淘金人足球队", "多伦多猛龙队", "多伦多竞技场队",
								"多伦多蓝鸟队", "多伦多足球俱乐部", "多哥", "多特蒙德足球俱乐部", "多米尼克·克鲁兹", "多米尼克·雷耶斯", "多米尼加",
								"多米尼加共和国", "多米尼加共和国队", "大分三神足球队", "大卫·布兰奇", "大宫松鼠足球俱乐部", "大峡谷大学羚羊队",
								"大湖潜鸟队", "大瀑布航海者队", "大邦孟买 HC 队", "大邦德里队", "大阪樱花队", "大阪钢巴足球俱乐部", "天使城足球俱乐部",
								"天普大学猫头鹰队", "太平洋大学拳击手队", "太平洋大学老虎队", "太平洋足球俱乐部", "奇塔代拉", "奎德城河盗队",
								"奥克兰大学金熊队", "奥克兰突袭者队", "奥克兰运动家队", "奥兰多城队", "奥兰多荣耀足球队", "奥兰多魔术队",
								"奥列克西·奥利尼克", "奥古斯塔格林杰克斯队", "奥地利", "奥尔巴尼大丹尼斯队", "奥尔康州立勇士队", "奥文斯·圣普勒",
								"奥斯汀皮埃州长队", "奥斯汀足球俱乐部", "奥本大学老虎队", "奥本达博岱队", "奥林匹亚科斯篮球俱乐部", "奥格斯堡足球俱乐部",
								"奥格登猛龙队", "奥罗尔罗伯茨大学金鹰队", "奥萨苏纳足球俱乐部", "奥里萨邦足球俱乐部", "奥马哈小牛队", "奥马哈风暴追赶者队",
								"委内瑞拉", "委内瑞拉队", "威克森林大学恶魔指挥官队", "威奇托州立大学震撼者队", "威尔士", "威尔明顿蓝岩队", "威尔逊·雷斯",
								"威尼斯足球俱乐部", "威廉姆斯车队", "威廉波特横锯队", "威廉玛丽学院部落队", "威斯康星大学绿湾凤凰队", "威斯康星密尔沃基黑豹队",
								"威斯康星獾队", "威斯康辛木纹响尾蛇队", "威根竞技足球俱乐部", "孟买印度人队", "孟买城足球俱乐部", "孟买火箭队",
								"孟买马哈拉蒂队", "孟加拉勇士队", "孟加拉国", "孟德斯托坚果队", "孟菲斯灰熊队", "孟菲斯红鸟队", "孟菲斯老虎队",
								"安东尼·佩蒂斯", "安东尼·史密斯", "安吉拉·希尔", "安哥拉", "安德烈·阿尔洛夫斯基", "安德里亚·李", "安提瓜和巴布达",
								"安纳托利亚艾菲斯伊斯坦布尔篮球队", "安纳海姆鸭队", "安道尔", "宾厄姆顿大都会队", "宾夕法尼亚大学贵格会队",
								"宾州州立大学尼塔尼雄狮队", "宾汉顿大学熊狸队", "密克罗尼西亚联邦", "密尔沃基勇士队", "密尔沃基老鹰队", "密尔沃基雄鹿队",
								"密尔瓦基酿酒人队", "密歇根大学狼獾队", "密歇根州立斯巴达人队", "密苏里大学堪萨斯城分校袋鼠队", "密苏里大学老虎队",
								"密苏里州鱼鹰队", "密西西比勇士队", "密西西比大学反叛者队", "密西西比大学叛逆者队", "密西西比州立大学斗牛犬队",
								"密西西比河谷州三角洲魔鬼队", "富勒姆足球俱乐部", "富山胜利足球队", "富恩拉布拉达足球俱乐部", "小安东尼奥·卡洛斯", "小轿车",
								"尤文图斯足球俱乐部", "尤西尔·福米加", "尤达队", "尤金绿宝石队", "尼亚加拉大学紫鹰队", "尼加拉瓜", "尼可·蒙塔尼奥",
								"尼基塔·克雷洛夫", "尼姆奥林匹克足球俱乐部", "尼娜·安萨罗夫", "尼尔·马格尼", "尼斯足球俱乐部", "尼日利亚", "尼日尔",
								"尼泊尔", "尼科尔斯州立大学上校队", "山口雷法足球俱乐部", "山形山神足球俱乐部", "岩手盛冈仙鹤足球俱乐部", "川崎前锋队",
								"州立大学尖峰队", "巴克内尔大学野牛队", "巴列卡诺足球俱乐部", "巴利亚多利德足球俱乐部", "巴勒斯坦", "巴勒莫", "巴哈马",
								"巴基斯坦", "巴塔维亚脏狗队", "巴塞尔足球俱乐部", "巴塞罗那篮球俱乐部", "巴塞罗那足球俱乐部", "巴尔的摩乌鸦队",
								"巴尔的摩金莺队", "巴巴多斯", "巴布亚新几内亚", "巴拉圭", "巴拿马草帽", "巴拿马队", "巴斯干尼亚篮球队",
								"巴斯蒂亚足球俱乐部", "巴林", "巴特勒大学斗牛犬队", "巴特那海盗队", "巴西", "巴里", "市原千叶足球俱乐部",
								"布伦特福德足球俱乐部", "布伦瑞克足球俱乐部", "布兰登·莫雷诺", "布基纳法索", "布拉加竞技俱乐部", "布拉德·塔瓦雷斯",
								"布拉德利勇士队", "布朗大学熊队", "布法罗大学公牛队", "布法罗比尔队", "布莱克本流浪者足球俱乐部", "布莱恩特大学斗牛犬队",
								"布莱顿足球俱乐部", "布赖恩·奥尔特加", "布里斯托尔城足球俱乐部", "布隆迪", "布雷斯特足球俱乐部", "布雷瓦德县海牛队",
								"布雷登顿掠夺者队", "布雷西亚足球俱乐部", "布鲁克林篮网队", "布鲁克林飓风队", "希伯尼安足球俱乐部", "希姆基莫斯科地区篮球队",
								"希洪竞技足球俱乐部", "希科里淡水龙虾队", "希腊", "帕劳", "帕尔蒂克足球俱乐部", "帕尔马足球俱乐部", "帕纳辛奈克斯篮球俱乐部",
								"帝王足球俱乐部", "广岛三箭足球队", "广岛东洋鲤鱼队", "库克群岛", "底特律大学泰坦队", "底特律活塞队", "底特律猎鹰队",
								"底特律红翼队", "底特律美洲狮队", "底特律老虎队", "底特律雄狮队", "底特律震动队", "庞费拉迪纳足球俱乐部", "康奈尔大红队",
								"康涅狄格大学哈士奇队", "康涅狄格太阳队", "康涅狄格州中部蓝魔队", "康涅狄格老虎队", "康纳·麦格雷戈", "开曼群岛",
								"弗兰基·埃德加", "弗吉尼亚军事学院军校生队", "弗吉尼亚大学骑士队", "弗吉尼亚理工大学霍奇队", "弗吉尼亚联邦大学公羊队",
								"弗曼大学圣骑士队", "弗朗西斯·纳干诺", "弗朗西斯科·特里纳尔多", "弗罗西诺内足球俱乐部", "弗莱堡足球俱乐部", "弗里斯科游骑兵队",
								"弗雷德里克钥匙队", "弗雷斯诺州立斗牛犬队", "弗雷斯诺灰熊队", "得州农工大学农夫队", "德保罗大学蓝色恶魔队",
								"德克萨斯农工大学科普斯克里斯蒂岛民队", "德克萨斯南方大学老虎队", "德克萨斯大学埃尔帕索矿工队", "德克萨斯大学泛美分校野马队",
								"德克萨斯大学长角牛队", "德克萨斯大学阿灵顿分校小牛队", "德克萨斯州立大学山猫队", "德克萨斯巡游者队", "德克萨斯理工大学红色突袭者队",
								"德克萨斯里奥格兰德河谷大学牛仔队", "德国", "德岛漩涡足球俱乐部", "德州基督教大学角蛙队", "德州大学圣安东尼奥分校路跑者队",
								"德比郡足球俱乐部", "德玛瓦水鸟队", "德米安·迈亚", "德累斯顿迪纳摩足球俱乐部", "德维森·菲格雷多", "德里乘波者队",
								"德里克·刘易斯", "德里克·布伦森", "德里苏丹队", "德里首都队", "德雷克大学斗牛犬队", "德雷塞尔大学龙队", "德顿龙队",
								"心脏", "怀俄明大学牛仔队", "恩波利足球俱乐部", "意大利", "慕尼黑 1860 足球俱乐部", "戴维森学院野猫队",
								"戴顿大学飞人队", "所罗门群岛", "扎比特·马戈梅德沙里波夫", "托尼·弗格森", "托尼亚·埃文格", "托特纳姆热刺足球俱乐部",
								"托莱多泥母鸡队", "托莱多火箭队", "托马斯·阿尔梅达", "扬·布拉霍维奇", "扬斯敦州立大学企鹅队", "拉奎尔·彭宁顿",
								"拉尼·叶海亚", "拉德福德大学高地人队", "拉斐尔·多斯·安霍斯", "拉斐尔·阿松桑", "拉斐特学院花豹队", "拉斯帕尔马斯足球俱乐部",
								"拉斯维加斯王牌队", "拉斯维加斯突袭者队", "拉斯维加斯飞行者队", "拉科鲁尼亚足球俱乐部", "拉脱维亚", "拉萨尔大学探险者队",
								"拉贾斯坦皇家队", "拉马尔大学红雀队", "拉齐奥", "拜仁慕尼黑篮球俱乐部", "拜仁慕尼黑足球俱乐部", "拜耳勒沃库森足球俱乐部",
								"挪威", "捷克共和国", "摩尔多瓦", "摩德纳足球俱乐部", "摩根州立大学熊队", "摩洛哥", "摩纳哥足球俱乐部", "文莱",
								"斋浦尔粉红豹队", "斯克兰顿/威尔克斯-巴里洋基队", "斯图加特足球俱乐部", "斯坦福大学深红队", "斯威士兰", "斯帕尔足球俱乐部",
								"斯托克城足球俱乐部", "斯托克顿港口队", "斯旺西城足球俱乐部", "斯普林菲尔德红雀队", "斯波坎印第安人队", "斯泰森大学制帽人队",
								"斯洛伐克", "斯洛文尼亚", "斯特凡·斯特鲁夫", "斯特拉斯堡足球俱乐部", "斯蒂普·米欧奇", "斯蒂芬·汤普森",
								"斯蒂芬奥斯汀州立大学伐木工人队", "斯里兰卡", "新不列颠石猫队", "新加坡", "新墨西哥大学罗伯斯队", "新墨西哥州立大学阿吉斯队",
								"新奥尔良圣徒队", "新奥尔良大学私掠船队", "新奥尔良鹈鹕队", "新泽西/纽约哥谭足球队", "新泽西州/纽约哥谭足球俱乐部",
								"新泽西理工学院高地人队", "新泽西篮网队", "新泽西魔鬼队", "新潟天鹅队", "新罕布什尔大学野猫队", "新罕布什尔捕鱼猫队",
								"新英格兰爱国者队", "新英格兰革命足球俱乐部", "新英格兰黑狼队", "新莱昂自治大学老虎足球俱乐部", "新西兰", "旁遮普足球俱乐部",
								"日本", "日本队", "旧金山49人队", "旧金山勇士队", "旧金山大学唐斯队", "旧金山巨人队", "昂热足球俱乐部",
								"昆尼匹克大学山猫队", "明尼苏达北极星队", "明尼苏达双城队", "明尼苏达大学金地鼠队", "明尼苏达山猫队", "明尼苏达森林狼队",
								"明尼苏达维京人队", "明尼苏达联", "明尼苏达荒野队", "普内里帕坦队", "普利茅斯足球俱乐部", "普拉斯基洋基队",
								"普林斯顿大学老虎队", "普林斯顿射线队", "普渡大学锅炉工队", "普罗维登斯学院修道士队", "普雷斯顿足球俱乐部", "智利",
								"曼哈顿学院贾斯珀队", "曼城足球俱乐部", "曼彻斯特联足球俱乐部", "朗伍德大学枪骑兵队", "朝阳海德拉巴队", "朝鲜", "木星锤头队",
								"本·阮", "本菲卡足球俱乐部", "札幌冈萨多足球队", "朱尼尔·多斯·桑托斯", "朱莉安娜·佩纳", "杜克大学蓝魔队",
								"杜兰大学绿浪队", "杜塞尔多夫足球俱乐部", "杨百翰大学美洲狮队", "杰克逊州立大学老虎队", "杰克逊维尔太阳队",
								"杰克逊维尔州立大学斗鸡队", "杰克逊维尔海豚队", "杰克逊维尔美洲虎队", "杰曼·德·兰达米", "杰皮旁遮普勇士队", "杰西卡·埃",
								"杰西卡·安德拉德", "杰西卡·罗斯·克拉克", "杰里米·斯蒂芬斯", "松本山雅足球俱乐部", "极限运动", "林奇堡山猫队",
								"果阿足球俱乐部", "柏太阳神足球队", "柏林欧绿保篮球俱乐部", "柏林赫塔足球俱乐部", "查塔努加观景峰队", "查尔斯顿南方海盗队",
								"查尔斯顿学院美洲狮队", "查尔斯顿河狗队", "查尔顿竞技足球俱乐部", "查德·门德斯", "柬埔寨", "柯蒂斯·布莱兹",
								"柳别尔齐胜利篮球俱乐部", "栃木足球俱乐部", "格兰布林州立大学老虎队", "格威内特勇士队", "格拉纳达足球俱乐部", "格林斯波罗蚱蜢队",
								"格林纳达", "格林维尔大道队", "格林维尔太空人队", "格洛弗·特谢拉", "格罗宁根足球俱乐部", "格鲁吉亚", "桑坦德竞技足球俱乐部",
								"桑德兰足球俱乐部", "桑托斯拉古纳俱乐部", "桑普多利亚足球俱乐部", "梅斯足球俱乐部", "梅赛德斯 AMG 马石油车队",
								"棕榈滩红雀队", "横滨 DeNA 海湾之星队", "横滨体育及文化会", "横滨水手队", "横滨足球俱乐部", "欧仁猫头鹰队",
								"欧力士野牛队", "欧塞尔足球俱乐部", "比利亚雷亚尔足球俱乐部", "比利时", "比勒费尔德足球俱乐部", "比林斯野马队", "比萨",
								"毕尔巴鄂竞技足球俱乐部", "毛里塔尼亚", "毛里求斯", "水户蜀葵足球队", "水晶宫足球俱乐部", "水牛城军刀", "水牛城勇士队",
								"水牛城强盗队", "水牛城野牛队", "汉堡足球俱乐部", "汉密尔顿老虎队", "汉密尔顿虎猫队", "汉普顿大学海盗队",
								"汉莎罗斯托克足球俱乐部", "汉诺威 96 足球俱乐部", "沃尔坎·厄兹德米尔", "沃尔夫斯堡足球俱乐部", "沃特福德足球俱乐部",
								"沃福德学院小猎狗队", "沙尔克 04 足球俱乐部", "沙特阿拉伯", "沙瓦那沙虫队", "沙米尔·阿卜杜拉希莫夫", "沙维什",
								"河床竞技俱乐部", "河床足球俱乐部", "法伦斯", "法兰克福足球俱乐部", "法国", "法拉利车队", "法马利康足球俱乐部", "波兰",
								"波塔基特红袜队", "波士顿凯尔特人队", "波士顿大学梗犬队", "波士顿学院鹰队", "波士顿棕熊队", "波士顿焗豆队", "波士顿红袜队",
								"波士顿食豆人队", "波多黎各", "波多黎各队", "波尔图足球俱乐部", "波尔多瓶", "波尔州立大学红雀队", "波托马克国民队",
								"波斯尼亚和黑塞哥维那", "波特兰伐木者足球俱乐部", "波特兰大学飞行员队", "波特兰州立大学维京人队", "波特兰开拓者队",
								"波特兰海狗队", "波特兰海狸队", "波特兰火焰队", "波特兰荆棘队", "波蒂莫嫩塞足球俱乐部", "波鸿足球俱乐部", "泰·特瓦萨",
								"泰伦·伍德利", "泰卢固泰坦队", "泰国", "泰尔纳纳", "泰森·佩德罗", "泰米尔塔莱瓦斯队", "泽维尔大学火枪手队",
								"泽西岛海岸蓝爪队", "洋杉激流麦子队", "洛威旋球队", "洛杉矶充电器", "洛杉矶公羊队", "洛杉矶国王队", "洛杉矶天使队",
								"洛杉矶快船队", "洛杉矶湖人队", "洛杉矶火花队", "洛杉矶足球俱乐部", "洛杉矶道奇队", "洛杉矶银河足球俱乐部", "洛杉矶闪电",
								"洛约拉马利蒙特大学雄狮队", "洛里昂足球俱乐部", "津巴布韦", "洪都拉斯", "流浪者足球俱乐部", "浦和红钻足球俱乐部",
								"浦那 7 王牌队", "海伦娜酿酒人队", "海伦芬体育俱乐部", "海军学院见习官队", "海地", "海得拉巴猎人队", "海得拉巴足球俱乐部",
								"海波因特大学黑豹队", "清水心跳足球队", "清水长尾鲛队", "渥太华参议员队", "渥太华竞技", "渥太华红黑队", "温哥华加人队",
								"温哥华加拿大人队", "温哥华勇士队", "温哥华白浪足球俱乐部", "温哥华足球俱乐部", "温尼伯喷射机队", "温尼伯荣军足球俱乐部",
								"温尼伯蓝色轰炸机足球队", "温斯罗普大学鹰队", "温斯顿塞勒姆冲刺队", "湘南丽海足球队", "澳大利亚", "火鸡肉",
								"热那亚足球俱乐部", "熊本深红足球俱乐部", "爱奥那学院盖尔斯队", "爱媛足球俱乐部", "爱尔兰", "爱沙尼亚", "爱荷华大学鹰眼队",
								"爱荷华小熊队", "爱荷华州立大学旋风队", "爱达荷大学破坏者队", "爱达荷州立大学孟加拉虎队", "爱达荷瀑布鹌鹑队", "牙买加",
								"特伦顿雷霆队", "特内里费足球俱乐部", "特拉华大学战斗蓝母鸡队", "特拉华州立大学黄蜂队", "特拉维夫马卡比篮球俱乐部",
								"特洛伊州立大学特洛伊人队", "特温特足球俱乐部", "特立尼达和多巴哥", "特西亚·托雷斯", "特鲁瓦足球俱乐部", "犹他大学犹特队",
								"犹他州立大学农人队", "犹他爵士队", "犹他皇家足球队", "犹他谷大学狼獾队", "独立奥林匹克运动员", "玛丽昂·雷诺",
								"玛拉·罗梅罗·博雷拉", "玻利维亚", "珂斯东孟加拉足球俱乐部", "班加罗尔公牛队", "班加罗尔猛龙队", "班加罗尔皇家挑战者俱乐部",
								"班加罗尔足球俱乐部", "理海谷野猪队", "琉球足球俱乐部", "瑞典", "瑞士", "瓜达拉哈拉竞技俱乐部", "瓦伦缇娜·舍甫琴科",
								"瓦伦西亚篮球俱乐部", "瓦伦西亚足球俱乐部", "瓦努阿图", "瓦尔帕莱索大学十字军队", "瓦尔韦克足球俱乐部", "瓦朗谢讷足球俱乐部",
								"瓦格纳学院海鹰队", "甘冈足球俱乐部", "甘纳·纳尔逊", "田纳西大学志愿者队", "田纳西大学查塔努加分校莫奇队",
								"田纳西大学科技金鹰队", "田纳西大学马丁分校天鹰队", "田纳西州立大学老虎队", "田纳西巨神队", "田纳西斯摩基队", "田纳西泰坦队",
								"甲府风林足球俱乐部", "电子竞技", "町田泽维亚足球俱乐部", "白俄罗斯", "白求恩库克曼大学野猫队", "百年绅士队", "百慕大",
								"皇家克什米尔足球俱乐部", "皇家奥维耶多足球俱乐部", "皇家盐湖城队", "皇家社会足球俱乐部", "皇家萨拉戈萨足球俱乐部", "皇家马德里",
								"皇家马德里篮球队", "皮亚琴察足球俱乐部", "皮奥里亚酋长队", "盐湖城蜜蜂队", "相模原体育俱乐部", "石溪大学海狼队",
								"磐城足球俱乐部", "磐田喜悦足球队", "神户胜利船队", "福冈软银鹰队", "福冈黄蜂足球俱乐部", "福岛联足球俱乐部",
								"福特汉姆大学公羊队", "秋田蓝闪电足球俱乐部", "科尔比·科文顿", "科尔盖特大学突袭者队", "科平州立老鹰队", "科摩罗",
								"科林蒂安保利斯塔体育会", "科森察", "科特尼·凯西", "科特布斯足球俱乐部", "科特迪瓦", "科珀斯克里斯蒂铁钩队", "科索沃",
								"科罗拉多大学老虎队", "科罗拉多州立大学公羊队", "科罗拉多急流队", "科罗拉多斯普林斯天空袜队", "科罗拉多水牛队",
								"科罗拉多洛矶山脉", "科罗拉多猛犸队", "科罗拉多雪崩队", "科莫足球俱乐部", "科迪·加布兰特", "科迪·斯塔曼", "科里·安德森",
								"秘鲁", "空军猎鹰队", "突尼斯", "立普斯康大学野牛队", "立陶宛", "第戎足球俱乐部", "米兰奥林匹亚篮球俱乐部",
								"米兰德斯足球俱乐部", "米尔沃尔足球俱乐部", "米尔萨德·贝克蒂奇", "米德兰摇滚猎犬队", "米德尔斯堡足球俱乐部", "米歇尔·沃特森",
								"米沙·齐尔库诺夫", "索肖足球俱乐部", "索马里", "约尔·罗梅罗", "约旦", "约瑟夫·贝纳维德兹", "约翰·多德森",
								"约翰·莫拉加", "约翰·莱因克尔", "约翰逊城红衣主教队", "纳什维尔天籁队", "纳什维尔掠夺者队", "纳什维尔足球俱乐部",
								"纳米比亚", "纽伦堡足球俱乐部", "纽卡斯尔联足球俱乐部", "纽约FC", "纽约喷气机队", "纽约大学山猫队", "纽约大学紫罗兰队",
								"纽约大都会队", "纽约尼克斯队", "纽约岛民队", "纽约巨人队", "纽约洋基队", "纽约游骑兵队", "纽约激流队",
								"纽约理工学院熊队", "纽约红牛队", "纽约美国人队", "纽约自由人队", "纽约高地人队", "维加斯黄金骑士队", "维尔京群岛",
								"维戈塞尔塔足球俱乐部", "维拉诺瓦野猫队", "维特斯职业足球基金会", "维罗纳足球俱乐部", "维萨利亚拉希德队", "绿湾包装工队",
								"缅因大学黑熊队", "缅甸", "网球", "罗伯特·惠特克", "罗伯特莫里斯殖民地队", "罗克珊·莫达菲里", "罗切斯特皇家队",
								"罗切斯特红翼队", "罗切斯特骑士鹰队", "罗布·冯特", "罗德岛大学公羊队", "罗斯·娜玛朱纳斯", "罗格斯红衣骑士队", "罗比·劳勒",
								"罗瑟勒姆足球俱乐部", "罗达 JC 足球俱乐部", "罗马体育俱乐部", "罗马勇士队", "罗马尼亚", "美因茨足球俱乐部", "美国",
								"美国大学老鹰队", "美国队", "美属萨摩亚", "美洲足球俱乐部", "群马草津温泉队", "老挝", "老道明大学君主队",
								"考文垂足球俱乐部", "耶鲁大学斗牛犬队", "肯塔基大学野猫队", "肯尼亚", "肯尼索州立猫头鹰队", "肯特州立大学金色光芒队",
								"艾佛列特绿袜队", "艾克隆航空队", "艾哈迈达巴德后卫队", "艾查威尔足球俱乐部", "艾森豪湖风暴队", "艾法托利车队",
								"艾琳·阿尔达娜", "艾维斯体育俱乐部", "芝加哥公牛队", "芝加哥天空队", "芝加哥小熊队", "芝加哥州立美洲狮队",
								"芝加哥洛约拉大学漫步者队", "芝加哥火焰队", "芝加哥熊队", "芝加哥牡鹿队", "芝加哥白袜队", "芝加哥白长袜队", "芝加哥红星队",
								"芝加哥西风队", "芝加哥黑鹰队", "芬兰", "苏丹", "苏格兰", "苏里南", "英国", "英属维尔京群岛", "范德比尔特大学船长队",
								"草原风光农工大学美洲豹队", "荷兰", "荷兰队", "莫亨巴根足球俱乐部", "莫尔黑德州立大学老鹰队", "莫斯科中央陆军篮球俱乐部",
								"莫斯科斯巴达克足球俱乐部", "莫桑比克", "莫比尔海湾熊队", "莫雷伦斯足球俱乐部", "莱万特足球俱乐部", "莱克兰飞虎队",
								"莱克郡队长队", "莱切", "莱加内斯足球俱乐部", "莱德大学野马队", "莱斯大学猫头鹰队", "莱斯特城足球俱乐部",
								"莱比锡红牛足球俱乐部", "莱特州立大学突袭者队", "莱科", "莱索托", "菲利斯·赫里格", "菲尔莱狄更斯大学魔鬼队",
								"菲尼克斯太阳队", "菲尼克斯水星队", "菲律宾", "萨克拉门托君主队", "萨克拉门托国王队", "萨克拉门托河猫队",
								"萨兰迪兵工厂足球俱乐部", "萨凡纳州立大学老虎队", "萨勒尼塔纳足球俱乐部", "萨姆休斯顿州立大学熊狸队", "萨姆福德大学斗牛犬队",
								"萨尔瓦多", "萨拉·麦克曼", "萨拉基利斯篮球俱乐部", "萨摩亚", "萨斯喀彻温热潮队", "萨斯喀彻温省骑兵队", "萨索罗足球俱乐部",
								"葡萄牙", "蒂丁路运动联合会足球俱乐部", "蒂亚戈·桑托斯", "蒂姆·埃利奥特", "蒂尔堡威廉二世足球俱乐部", "蒙古",
								"蒙哥马利饼干队", "蒙大拿州立大学山猫队", "蒙大拿灰熊队", "蒙彼利埃足球俱乐部", "蒙扎", "蒙特利尔云雀队", "蒙特利尔冲击",
								"蒙特利尔加拿大人队", "蒙特利尔博览会队", "蒙特利尔流浪者队", "蒙特利尔褐红队", "蒙特雷足球俱乐部", "蒙茅斯大学老鹰队",
								"蓝十字竞技俱乐部", "蓝田金莺队", "藤枝 MYFC 足球俱乐部", "西伊利诺伊大学海军陆战队员队", "西北大学野猫队",
								"西北州立大学恶魔队", "西北阿肯色自然队", "西南密苏里州立大学熊队", "西卡罗莱纳大学大山猫队", "西印度群岛", "西密歇根大学野马队",
								"西密歇根白帽队", "西布罗姆维奇足球俱乐部", "西弗吉尼亚力量队", "西弗吉尼亚大学登山人队", "西拉丘斯国民队", "西汉姆联足球俱乐部",
								"西班牙", "西班牙人足球俱乐部", "西田纳西钻石工队", "西肯塔基大学山顶队", "西贾拉·尤班克斯", "西雅图大学红鹰队",
								"西雅图水手队", "西雅图海妖队", "西雅图海湾人足球俱乐部", "西雅图海鹰队", "西雅图超音速队", "西雅图风暴队",
								"要塞军事学院斗牛犬队", "詹妮弗·迈亚", "詹姆斯·维克", "詹姆斯敦干扰者队", "詹姆斯麦迪逊大学公爵队", "詹谢普尔足球俱乐部",
								"诺丁汉森林足球俱乐部", "诺福克州立大学斯巴达人队", "诺福克潮汐队", "读卖巨人队", "谢菲尔德星期三足球俱乐部",
								"谢菲尔德联足球俱乐部", "象牙海岸", "豪尔赫·马斯维达尔", "贝伦人足球俱乐部", "贝克斯菲尔德火焰队", "贝勒大学熊队", "贝宁",
								"贝尔格莱德红星篮球俱乐部", "贝尔蒙特大学棕熊队", "贝洛伊特鲷鱼队", "贝瑟·科雷娅", "贝茨山猫队", "贝蒂斯塞维利亚足球俱乐部",
								"费内巴切伊斯坦布尔篮球俱乐部", "费城76人队", "费城之翼队", "费城老鹰队", "费城联足球俱乐部", "费城贵格会队",
								"费城费城人队", "费城运动家队", "费城飞人队", "费尔利狄金森大学骑士队", "费尔菲尔德大学雄鹿队", "费耶诺德鹿特丹足球俱乐部",
								"费雷拉", "费雷拉柏苏斯足球俱乐部", "贾卡雷·苏扎", "贾斯汀·加瑟基", "贾斯汀·威利斯", "贾里德·坎诺尼尔",
								"赞岐釜玉海足球队", "赞比亚", "赤道几内亚", "赫塔菲足球俱乐部", "赫尔城足球俱乐部", "赫拉克勒斯足球俱乐部",
								"赫罗纳足球俱乐部", "越南", "足球俱乐部东京", "路易斯安那东南狮队", "路易斯安那大学拉法叶拉金卡津队",
								"路易斯安那大学门罗分校战鹰队", "路易斯安那州立大学老虎队", "路易斯安那州门罗大学印第安人队", "路易斯安那理工大学牛头犬队",
								"路易斯维尔大学红雀队", "路易斯维尔蝙蝠队", "路易斯维尔赛车队", "辛辛那提大学熊狸队", "辛辛那提猛虎队", "辛辛那提红人队",
								"辛辛那提红腿队", "辛辛那提足球俱乐部", "达伦·埃尔金斯", "达伦·蒂尔", "达勒姆公牛队", "达拉斯德州人队", "达拉斯星队",
								"达拉斯浸会大学爱国者队", "达拉斯牛仔队", "达拉斯独行侠队", "达拉斯足球俱乐部", "达拉斯飞翼队", "达斯汀·奥尔蒂斯",
								"达斯汀·波里耶", "达特茅斯大绿队", "迈克尔·基耶萨", "迈尔斯堡奇迹队", "迈阿密国际足球俱乐部", "迈阿密大学飓风队",
								"迈阿密大学（俄亥俄州）红鹰队", "迈阿密太阳神队", "迈阿密海豚队", "迈阿密热火队", "迈阿密马林鱼队", "进击的浦那超级巨人队",
								"迪加史卓普足球队", "迪尤肯大学公爵队", "通德拉足球俱乐部", "道格拉斯·席尔瓦·德·安德拉德", "邓弗姆林竞技足球俱乐部",
								"邓迪联足球俱乐部", "邓迪足球俱乐部", "那不勒斯足球俱乐部", "郑赞盛", "都灵", "里卡多·拉马斯", "里士满大学蜘蛛队",
								"里士满飞鼠队", "里奥艾维足球俱乐部", "里尔足球俱乐部", "里昂·爱德华兹", "里昂足球俱乐部", "金士顿印第安人队",
								"金奈银足球俱乐部", "金州勇士队", "金斯波特大都会队", "金泽塞维根足球俱乐部", "金翼鹦鹉", "钦奈城足球俱乐部",
								"钦奈斯巴达人队", "钦奈超级国王队", "钦奈超级星队", "锡耶纳学院圣徒队", "镜片", "长岛大学黑鸟队", "长崎成功丸足球俱乐部",
								"长滩州立大学 49 人队", "长滩州立大学鲨鱼队", "长老会学院蓝色软管队", "长野帕塞罗体育俱乐部", "门兴格拉德巴赫足球俱乐部",
								"阅读", "阪神虎队", "阿什维尔旅行者队", "阿什莉·埃文斯-史密斯", "阿伯丁足球俱乐部", "阿伯丁铁鸟队", "阿克伦大学拉链靴队",
								"阿利斯泰尔·奥弗瑞姆", "阿塞拜疆", "阿富汗", "阿尔·亚昆塔", "阿尔伯克基同位素队", "阿尔克马尔足球俱乐部", "阿尔及利亚",
								"阿尔图纳曲线队", "阿尔巴塞特足球俱乐部", "阿尔巴尼亚", "阿尔梅里亚足球俱乐部", "阿尔法罗密欧车队", "阿尔科孔足球俱乐部",
								"阿巴拉契亚州登山者队", "阿拉伯联合酋长国", "阿拉巴马农工大学斗牛犬队", "阿拉巴马大学伯明翰分校开拓者队", "阿拉巴马州立大学黄蜂队",
								"阿拉巴马赤潮队", "阿拉维斯足球俱乐部", "阿斯彭·莱德", "阿斯科利", "阿斯顿维拉足球俱乐部", "阿曼", "阿曼达·努内斯",
								"阿根廷", "阿森纳足球俱乐部", "阿比林基督教大学野猫队", "阿特拉斯足球俱乐部", "阿瓦德勇士队", "阿罗卡",
								"阿肯色大学松树壁分校金狮队", "阿肯色大学野猪队", "阿肯色州中部熊队", "阿肯色州小石城特洛伊队", "阿肯色州立大学印地安人队",
								"阿肯色州立大学红狼队", "阿肯色旅行者队", "阿贾克斯足球俱乐部", "阿贾曼·斯特林", "阿雅克肖足球俱乐部", "阿鲁巴岛",
								"陆军黑骑士队", "陶森大学老虎队", "难民奥运队", "雅吉玛熊队", "雪城大学橙人队", "雪城酋长队", "雷·博格", "雷丁费城人队",
								"雷吉纳足球俱乐部", "雷恩足球俱乐部", "雷纳托·莫伊卡诺", "雷诺 DP World 车队", "雷诺王牌队", "霍华德大学野牛队",
								"霍夫斯特拉大学骄傲队", "霍夫斯特拉飞行荷兰人队", "霍芬海姆足球俱乐部", "霍莉·霍尔姆", "露西·普迪洛娃", "韦伯州立野猫队",
								"韦尔瓦足球俱乐部", "韦恩堡乳齿象队", "韦恩堡活塞队", "韦恩堡锡帽队", "韦斯卡足球俱乐部", "韩国", "韩国队", "香港",
								"马修斯·尼科劳", "马克·亨特", "马克斯·霍洛威", "马其顿", "马奎特大学金鹰队", "马尔代夫", "马德里竞技足球俱乐部",
								"马拉加足球俱乐部", "马拉维", "马来西亚", "马歇尔大学雷霆追风队", "马瑟韦尔足球俱乐部", "马略卡足球俱乐部", "马绍尔群岛",
								"马耳他", "马萨诸塞大学民兵队", "马赛足球俱乐部", "马辛·泰布拉", "马达加斯加", "马里", "马里兰大学东岸分校战鹰队",
								"马里兰大学巴尔的摩县猎犬队", "马里兰大学水龟队", "马里兰洛约拉大学灰狗队", "马里斯特学院红狐狸队", "马里迪莫体育俱乐部",
								"马霍宁谷拳击手队", "马龙·莫拉斯", "高中棒球队", "高山", "高沙漠小牛队", "高知蓝强攻队", "鲍伊贝索克斯队",
								"鲍灵格林热棒队", "鲍灵格林猎鹰队", "鸟取飞翔足球俱乐部", "鸟栖砂岩足球队", "鹿岛鹿角足球俱乐部", "鹿特丹精英足球俱乐部",
								"麦克尼斯州立大学牛仔队", "麦肯齐·邓恩", "麻省大学罗威尔分校河鹰队", "黎巴嫩", "黑山", "黑格斯敦太阳队", "黑鹰海得拉巴队",
								"默特尔比奇鹈鹕队", "默瑟大学熊队", "默里州立大学赛车手队"
							]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"recommended_uses_for_product": {
			"title": "建议用法",
			"description": "从有效值列表中选择。",
			"examples": ["仅在室内使用"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 11,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "建議用法",
						"description": "从有效值列表中选择。",
						"editable": true,
						"hidden": false,
						"examples": ["仅在室内使用"],
						"type": "string",
						"maxLength": 2329,
						"anyOf": [{
							"type": "string"
						}, {
							"type": "string",
							"enum": ["固定頭髮", "暖暖帽", "淋浴", "美髮", "耳朵保暖", "面部護理"],
							"enumNames": ["固定頭髮", "暖暖帽", "淋浴", "美髮", "耳朵保暖", "面部護理"]
						}]
					}
				},
				"additionalProperties": false
			}
		},
		"scent": {
			"title": "香型",
			"description": "如果您的商品按照不同香型被区分为某父商品的子商品，请使用该输入项说明该子商品的香型。",
			"examples": ["清新"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "芳香",
						"description": "给定商品的气味说明",
						"editable": true,
						"hidden": false,
						"examples": ["薰衣草、麝香"],
						"type": "string",
						"maxLength": 2200
					}
				},
				"additionalProperties": false
			}
		},
		"grip": {
			"title": "握杆",
			"description": "该属性表示产品的握把",
			"examples": ["橡胶"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"type": {
						"title": "握把类型",
						"description": "给出产品的握把类型",
						"examples": ["绝缘，非绝缘"],
						"type": "array",
						"minItems": 1,
						"minUniqueItems": 1,
						"maxUniqueItems": 1,
						"selectors": ["language_tag"],
						"items": {
							"type": "object",
							"required": ["language_tag", "value"],
							"properties": {
								"language_tag": {
									"$ref": "#/$defs/language_tag"
								},
								"value": {
									"title": "握把类型",
									"description": "给出产品的握把类型",
									"editable": true,
									"hidden": false,
									"examples": ["绝缘，非绝缘"],
									"type": "string",
									"maxUtf8ByteLength": 2000
								}
							},
							"additionalProperties": false
						}
					}
				},
				"additionalProperties": false
			}
		},
		"item_width": {
			"title": "商品宽度",
			"description": "提供商品在即用状态下的宽度。",
			"examples": ["92 厘米"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["unit", "value"],
				"properties": {
					"value": {
						"title": "商品宽度",
						"description": "提供商品在即用状态下的宽度。",
						"editable": true,
						"hidden": false,
						"examples": ["92"],
						"type": "number",
						"minimum": 0,
						"maximum": 400000
					},
					"unit": {
						"title": "宽度单位",
						"description": "提供用于测量商品宽度的测量单位。",
						"editable": true,
						"hidden": false,
						"examples": ["厘米, 英寸"],
						"type": "string",
						"enum": ["inches"],
						"enumNames": ["英寸"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"master_pack_dimensions": {
			"title": "主包装尺寸",
			"description": "提供主包装的宽度、高度和深度尺寸。",
			"examples": ["32.4英寸"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["height", "length", "width"],
				"properties": {
					"height": {
						"title": "主包装高度",
						"description": "提供主包装的高度尺寸。",
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "主包装高度单位",
								"description": "说明用于指定主包装高度测量值的相应单位。",
								"editable": true,
								"hidden": false,
								"examples": ["英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "主包装高度",
								"description": "提供主包装的高度尺寸。",
								"editable": true,
								"hidden": false,
								"examples": ["32.4"],
								"type": "number",
								"minimum": 0,
								"maximum": 10000,
								"maxLength": 16,
								"multipleOf": 1e-12
							}
						},
						"additionalProperties": false
					},
					"length": {
						"title": "主包装长度",
						"description": "提供主包装的长度尺寸。",
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "主包装长度单位",
								"description": "说明用于指定主包装长度测量值的相应单位。",
								"editable": true,
								"hidden": false,
								"examples": ["英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "主包装长度",
								"description": "提供主包装的长度尺寸。",
								"editable": true,
								"hidden": false,
								"examples": ["32.4"],
								"type": "number",
								"minimum": 0,
								"maximum": 10000,
								"maxLength": 16,
								"multipleOf": 1e-12
							}
						},
						"additionalProperties": false
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"width": {
						"title": "主包装宽度",
						"description": "提供主包装的宽度尺寸。",
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "主包装宽度单位",
								"description": "说明用于指定主包装宽度测量值的相应单位。",
								"editable": true,
								"hidden": false,
								"examples": ["英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "主包装宽度",
								"description": "提供主包装的宽度尺寸。",
								"editable": true,
								"hidden": false,
								"examples": ["32.4"],
								"type": "number",
								"minimum": 0,
								"maximum": 10000,
								"maxLength": 16,
								"multipleOf": 1e-12
							}
						},
						"additionalProperties": false
					}
				},
				"additionalProperties": false
			}
		},
		"master_pack_weight": {
			"title": "主包装重量",
			"description": "提供主包装商品的重量测量。",
			"examples": ["25.3磅"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["unit", "value"],
				"properties": {
					"value": {
						"title": "主包装重量",
						"description": "提供主包装商品的重量测量。",
						"editable": true,
						"hidden": false,
						"examples": ["25.3"],
						"type": "number",
						"minimum": 0,
						"maximum": 10000,
						"maxLength": 16,
						"multipleOf": 1e-12
					},
					"unit": {
						"title": "主包装重量单位",
						"description": "说明用于指定主包装商品重量的相应单位。",
						"editable": true,
						"hidden": false,
						"examples": ["磅"],
						"type": "string",
						"enum": ["grams", "kilograms", "tons", "milligrams", "hundredths_pounds", "ounces", "pounds"],
						"enumNames": ["克", "千克", "吨", "毫克", "百分之一磅", "盎司", "磅"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"parentage_level": {
			"title": "父条目的等级",
			"description": "注明该库存单位是指父条目还是子条目",
			"examples": ["父条目"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "父条目的等级",
						"description": "注明该库存单位是指父条目还是子条目",
						"editable": true,
						"hidden": false,
						"examples": ["父条目"],
						"type": "string",
						"enum": ["child", "parent"],
						"enumNames": ["儿童", "父条目"]
					}
				},
				"additionalProperties": false
			}
		},
		"child_parent_sku_relationship": {
			"title": "子条目的关系类型",
			"description": "提供关系类型",
			"examples": ["附带品"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["child_relationship_type"],
				"properties": {
					"child_relationship_type": {
						"title": "子条目的关系类型",
						"description": "父条目与子条目之间的关系",
						"editable": true,
						"hidden": false,
						"examples": ["配件"],
						"type": "string",
						"enum": ["variation"],
						"enumNames": ["变化"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"parent_sku": {
						"title": "父条目的库存单位",
						"description": "父条目的库存单位",
						"editable": true,
						"hidden": false,
						"examples": ["ABC123"],
						"type": "string",
						"maxUtf8ByteLength": 5000
					}
				},
				"additionalProperties": false
			}
		},
		"variation_theme": {
			"title": "变体主题",
			"description": "指定产品将使用的变体主题。主题属性必须应用于分组中的所有商品。",
			"examples": ["Size/Color"],
			"type": "array",
			"minItems": 1,
			"maxItems": 1,
			"items": {
				"type": "object",
				"required": ["name"],
				"properties": {
					"name": {
						"title": "变体主题名称",
						"description": "指定产品将使用的变体主题。主题属性必须应用于分组中的所有商品。",
						"$lifecycle": {
							"enumDeprecated": ["AGE_GENDER_CATEGORY", "COLOR", "COLOR/NUMBER_OF_ITEMS", "COLOR_NAME",
								"COLOR_NAME/FLAVOR_NAME", "COLOR_NAME/ITEM_DISPLAY_LENGTH",
								"COLOR_NAME/ITEM_PACKAGE_QUANTITY", "COLOR_NAME/MATERIAL_TYPE",
								"COLOR_NAME/METAL_TYPE", "COLOR_NAME/NUMBER_OF_ITEMS", "COLOR_NAME/PATTERN_NAME",
								"COLOR_NAME/RING_SIZE", "COLOR_NAME/SIZE_NAME", "COLOR_NAME/SIZE_NAME/STYLE_NAME",
								"COLOR_NAME/STYLE_NAME", "EDITION", "FLAVOR_NAME", "GRIP_TYPE",
								"ITEM_DISPLAY_DIAMETER", "ITEM_DISPLAY_LENGTH", "ITEM_FORM", "ITEM_FORM/SIZE_NAME",
								"ITEM_PACKAGE_QUANTITY", "ITEM_PACKAGE_QUANTITY/COLOR_NAME", "ITEM_SHAPE",
								"ITEM_WEIGHT", "LENS_COLOR", "MATERIAL_TYPE", "MATERIAL_TYPE/COLOR_NAME",
								"MATERIAL_TYPE/ITEM_FORM/SIZE_NAME", "MATERIAL_TYPE/SIZE_NAME", "METAL_TYPE",
								"MODEL", "MODEL/SIZE_NAME", "MODEL_NUMBER/SIZE", "NUMBER_OF_ITEMS",
								"NUMBER_OF_ITEMS/STYLE_NAME", "PATTERN", "PATTERN_NAME", "PATTERN_NAME/COLOR_NAME",
								"RING_SIZE", "SCENT_NAME", "SIZE", "SIZE/COLOR", "SIZE/COLOR/NUMBER_OF_ITEMS",
								"SIZE/NUMBER_OF_ITEMS", "SIZE/UNIT_COUNT", "SIZE_NAME", "SIZE_NAME/COLOR_NAME",
								"SIZE_NAME/COLOR_NAME/NUMBER_OF_ITEMS", "SIZE_NAME/COLOR_NAME/PATTERN_NAME",
								"SIZE_NAME/MATERIAL_TYPE", "SIZE_NAME/NUMBER_OF_ITEMS", "SIZE_NAME/SCENT_NAME",
								"SIZE_NAME/STYLE_NAME", "SIZE_NAME/STYLE_NAME/COLOR_NAME", "SIZE_NAME/UNIT_COUNT",
								"SPECIAL_SIZE_TYPE/SIZE_NAME/COLOR_NAME", "STYLE_NAME", "STYLE_NAME/COLOR_NAME",
								"STYLE_NAME/SIZE_NAME", "TEAM_NAME", "TEAM_NAME/SIZE_NAME",
								"TEAM_NAME/SIZE_NAME/COLOR_NAME", "VOLTAGE", "VOLUME_CAPACITY_NAME"
							]
						},
						"editable": true,
						"hidden": false,
						"examples": ["Size/Color"],
						"type": "string",
						"enum": ["SIZE/NUMBER_OF_ITEMS", "VOLUME_CAPACITY_NAME", "TEAM_NAME", "TEAM_NAME/SIZE_NAME",
							"TEAM_NAME/SIZE_NAME/COLOR_NAME", "PATTERN", "PATTERN_NAME", "PATTERN_NAME/COLOR_NAME",
							"MODEL", "MODEL/SIZE_NAME", "MODEL_NUMBER/SIZE", "SIZE/UNIT_COUNT",
							"SIZE/COLOR/NUMBER_OF_ITEMS", "AGE_GENDER_CATEGORY", "RING_SIZE", "GRIP_TYPE",
							"MATERIAL_TYPE", "MATERIAL_TYPE/ITEM_FORM/SIZE_NAME", "MATERIAL_TYPE/SIZE_NAME",
							"MATERIAL_TYPE/COLOR_NAME", "STYLE_NAME", "STYLE_NAME/SIZE_NAME",
							"STYLE_NAME/COLOR_NAME", "SCENT_NAME", "EDITION", "ITEM_PACKAGE_QUANTITY",
							"ITEM_PACKAGE_QUANTITY/COLOR_NAME", "ITEM_DISPLAY_DIAMETER", "ITEM_DISPLAY_LENGTH",
							"ITEM_FORM", "ITEM_FORM/SIZE_NAME", "ITEM_SHAPE", "NUMBER_OF_ITEMS",
							"NUMBER_OF_ITEMS/STYLE_NAME", "ITEM_WEIGHT", "SPECIAL_SIZE_TYPE/SIZE_NAME/COLOR_NAME",
							"VOLTAGE", "SIZE", "SIZE/COLOR", "SIZE_NAME", "SIZE_NAME/UNIT_COUNT",
							"SIZE_NAME/MATERIAL_TYPE", "SIZE_NAME/STYLE_NAME", "SIZE_NAME/STYLE_NAME/COLOR_NAME",
							"SIZE_NAME/SCENT_NAME", "SIZE_NAME/NUMBER_OF_ITEMS", "SIZE_NAME/COLOR_NAME",
							"SIZE_NAME/COLOR_NAME/PATTERN_NAME", "SIZE_NAME/COLOR_NAME/NUMBER_OF_ITEMS",
							"METAL_TYPE", "LENS_COLOR", "COLOR", "COLOR/NUMBER_OF_ITEMS", "COLOR/SIZE",
							"COLOR_NAME", "COLOR_NAME/PATTERN_NAME", "COLOR_NAME/RING_SIZE",
							"COLOR_NAME/MATERIAL_TYPE", "COLOR_NAME/STYLE_NAME", "COLOR_NAME/ITEM_PACKAGE_QUANTITY",
							"COLOR_NAME/ITEM_DISPLAY_LENGTH", "COLOR_NAME/NUMBER_OF_ITEMS", "COLOR_NAME/SIZE_NAME",
							"COLOR_NAME/SIZE_NAME/STYLE_NAME", "COLOR_NAME/METAL_TYPE", "COLOR_NAME/FLAVOR_NAME",
							"FLAVOR_NAME"
						],
						"enumNames": ["SIZE/NUMBER_OF_ITEMS", "体积_容积_名称", "团队_名称", "团队_名称/规格_名称", "团队_名称/规格_名称/颜色_名称",
							"图案", "图案_名称", "图案_名称/颜色_名称", "型号", "型号/规格_名称", "型号_编号/尺寸", "尺寸/单位_计数", "尺寸/颜色/商品数量",
							"年龄_性别_种类", "戒指_尺寸", "握柄_类型", "材料_类型", "材料_类型/物品_形态/规格_名称", "材料_类型/规格_名称",
							"材料_类型/颜色_名称", "款式_名称", "款式_名称/规格_名称", "款式_名称/颜色_名称", "气味_名称", "版次", "物品_包装_数量",
							"物品_包装_数量/颜色_名称", "物品_展示_直径", "物品_展示_长度", "物品_形态", "物品_形态/规格_名称", "物品_形状", "物品_数量",
							"物品_数量/款式_名称", "物品_重量", "特殊_尺码_类型/规格_名称/颜色_名称", "电压", "规格", "规格/颜色", "规格_名称",
							"规格_名称/单位_计数", "规格_名称/材料_类型", "规格_名称/款式_名称", "规格_名称/款式_名称/颜色_名称", "规格_名称/气味_名称",
							"规格_名称/物品_数量", "规格_名称/颜色_名称", "规格_名称/颜色_名称/图案_名称", "规格_名称/颜色_名称/物品_数量", "金属_类型",
							"镜片_颜色", "颜色", "颜色/商品数量", "颜色/尺寸", "颜色_名称", "颜色_名称/图案_名称", "颜色_名称/戒指_尺寸", "颜色_名称/材料_类型",
							"颜色_名称/款式_名称", "颜色_名称/物品_包装_数量", "颜色_名称/物品_展示_长度", "颜色_名称/物品_数量", "颜色_名称/规格_名称",
							"颜色_名称/规格_名称/款式_名称", "颜色_名称/金属_类型", "颜色_名称/风味_名称", "风味_名称"
						]
					}
				},
				"additionalProperties": false
			}
		},
		"country_of_origin": {
			"title": "原产国",
			"description": "生产该商品的国家。",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "原产国",
						"description": "选择产品的原产国",
						"editable": false,
						"hidden": false,
						"examples": ["日本，英国，美国"],
						"type": "string",
						"enum": ["GB", "TL", "WD", "WZ", "XK", "XM", "BT", "VI", "VG", "TP", "CN", "CF", "DK", "UA",
							"UZ", "UG", "UY", "TD", "YE", "AM", "IL", "IQ", "IR", "BZ", "CV", "RU", "BG", "HR",
							"GU", "GM", "IS", "GN", "GW", "LI", "CG", "CD", "LY", "LR", "CA", "GH", "GA", "HU",
							"MP", "GS", "YU", "AQ", "SS", "ZA", "BQ", "BW", "QA", "RW", "LU", "IN", "ID", "GT",
							"EC", "ER", "SY", "CU", "TW", "SZ", "KG", "DJ", "TV", "KZ", "CO", "CR", "CM", "TM",
							"TR", "LC", "KN", "ST", "XE", "XY", "BL", "VC", "PM", "CX", "SH", "MF", "SX", "SM",
							"GY", "TZ", "EG", "ET", "KI", "TJ", "SN", "RS", "CS", "SL", "CY", "SC", "MX", "TG",
							"DM", "DO", "AT", "VE", "BD", "AO", "AI", "AG", "AD", "FM", "NI", "NG", "NE", "NP",
							"XN", "PS", "BS", "PK", "BB", "PG", "PY", "PA", "BH", "BR", "BF", "BV", "GR", "PW",
							"CK", "CW", "XC", "KY", "DE", "IT", "SB", "ZR", "TK", "LV", "NO", "CZ", "MD", "MA",
							"MC", "BN", "FJ", "SK", "SI", "SJ", "LK", "SG", "NC", "NZ", "JP", "CL", "IM", "KP",
							"unknown", "KH", "GG", "GD", "GL", "GE", "VA", "BE", "MR", "MU", "TO", "SA", "FR", "TF",
							"PF", "GF", "FO", "PL", "XB", "PR", "BA", "TH", "JE", "ZW", "HN", "HT", "AU", "MO",
							"IE", "EE", "JM", "TC", "TT", "TA", "BO", "NR", "SE", "CH", "GP", "WF", "VU", "RE",
							"BY", "BM", "PN", "GI", "FK", "KW", "KM", "CI", "CC", "PE", "TN", "LT", "SO", "JO",
							"NA", "NU", "MM", "RO", "US", "UM", "AS", "LA", "KE", "FI", "SD", "SR", "UK", "IO",
							"NL", "AN", "MZ", "LS", "PH", "SV", "WS", "PT", "MN", "MS", "BI", "EH", "ES", "IC",
							"NF", "BJ", "ZM", "GQ", "HM", "VN", "AX", "AZ", "AF", "DZ", "AL", "AE", "OM", "AR",
							"AC", "AW", "KR", "HK", "MK", "MV", "MW", "MQ", "MY", "YT", "MH", "MT", "MG", "ML",
							"LB", "ME"
						],
						"enumNames": ["GB", "Timor-Leste", "WD", "WZ", "XK", "XM", "不丹", "不列颠岛(美)", "不列颠岛(英)", "东帝汶",
							"中国", "中非共和国", "丹麦", "乌克兰", "乌兹别克斯坦", "乌干达", "乌拉圭", "乍得", "也门", "亚美尼亚", "以色列", "伊拉克",
							"伊朗", "伯利兹", "佛得角", "俄罗斯", "保加利亚", "克罗地亚", "关岛", "冈比亚", "冰岛", "几内亚", "几内亚比绍", "列支敦士登",
							"刚果", "刚果民主共和国", "利比亚", "利比里亚", "加拿大", "加纳", "加蓬", "匈牙利", "北马里亚纳群岛共荣邦", "南乔治亚和南德桑威奇群岛",
							"南斯拉夫", "南极洲", "南苏丹", "南非", "博奈尔岛、圣尤斯达蒂斯和萨巴岛", "博茨瓦纳", "卡塔尔", "卢旺达", "卢森堡", "印度",
							"印度尼西亚", "危地马拉 (瓜地馬拉)", "厄瓜多尔", "厄立特里亚", "叙利亚", "古巴", "台湾", "史瓦济兰", "吉尔吉斯斯坦", "吉布提",
							"吐瓦鲁", "哈萨克斯坦", "哥伦比亚", "哥斯达黎加", "喀麦隆", "土库曼", "土耳其", "圣卢西亚", "圣基茨和尼维斯", "圣多美普林西比",
							"圣尤斯塔提马斯岛", "圣巴特勒米岛", "圣巴特岛", "圣文森特和格林纳丁斯", "圣皮埃尔和密克隆岛", "圣诞岛", "圣赫勒那", "圣马丁", "圣马丁岛",
							"圣马力诺", "圭亚那", "坦桑尼亚", "埃及", "埃塞俄比亚", "基里巴斯", "塔吉克斯坦", "塞内加尔", "塞尔维亚", "塞尔维亚和黑山",
							"塞拉利昂", "塞浦路斯", "塞舌尔", "墨西哥", "多哥", "多米尼克", "多米尼加共和国", "奥地利", "委内瑞拉", "孟加拉国", "安哥拉",
							"安圭拉", "安提瓜和巴布达", "安道尔", "密克罗尼西亚", "尼加拉瓜", "尼日利亚", "尼日尔", "尼泊尔", "尼维斯岛", "巴勒斯坦", "巴哈马",
							"巴基斯坦", "巴巴多斯", "巴布亚新几内亚", "巴拉圭", "巴拿马", "巴林", "巴西", "布基纳法索", "布韦岛", "希腊", "帕劳", "库克群岛",
							"库拉索岛", "库拉索岛(荷兰)", "开曼群岛", "德国", "意大利", "所罗门群岛", "扎伊尔", "托克劳", "拉脱维亚", "挪威", "捷克共和国",
							"摩尔多瓦", "摩洛哥", "摩纳哥", "文莱达鲁萨兰国", "斐济", "斯洛伐克", "斯洛文尼亚", "斯瓦尔巴特和扬马延群岛", "斯里兰卡", "新加坡",
							"新喀里多尼亚", "新西兰", "日本", "智利", "曼岛", "朝鲜", "未来之城", "柬埔寨", "根西岛", "格林纳达", "格陵兰", "格鲁吉亚",
							"梵帝冈", "比利时", "毛里塔尼亚", "毛里求斯", "汤加", "沙特阿拉伯", "法国", "法属南方领土", "法属波利尼西亚", "法屬圭亞那",
							"法罗群岛", "波兰", "波内尔岛", "波多黎各", "波斯尼亚 - 黑塞哥维那", "泰国", "泽西岛", "津巴布韦", "洪都拉斯", "海地", "澳大利亚",
							"澳门", "爱尔兰", "爱沙尼亚", "牙买加", "特克斯和凯科斯群岛", "特立尼达和多巴哥", "特里斯坦-达库尼亚群岛", "玻利维亚", "瑙鲁", "瑞典",
							"瑞士", "瓜德罗普", "瓦利斯和富图纳群岛", "瓦努阿图", "留尼汪", "白俄罗斯", "百慕达群岛", "皮特开恩群岛", "直布罗陀",
							"福克兰群岛（马尔维纳斯）", "科威特", "科摩罗", "科特迪瓦", "科科斯（基林）群岛", "秘鲁", "突尼斯", "立陶宛", "索马里", "约旦",
							"纳米比亚", "纽埃", "缅甸", "罗马尼亚", "美国", "美国边远小岛", "美属萨摩亚", "老挝", "肯尼亚", "芬兰", "苏丹", "苏里南",
							"英国", "英联邦的印度洋领域", "荷兰", "荷属安的列斯", "莫桑比克", "莱索托", "菲律宾", "萨尔瓦多", "萨摩亚", "葡萄牙", "蒙古",
							"蒙特塞拉特岛", "蒲隆地", "西撒哈拉", "西班牙", "西班牙加那利群岛", "诺福克岛", "贝宁", "赞比亚", "赤道几内亚", "赫德和麦克唐纳群岛",
							"越南", "阿兰群岛", "阿塞拜疆", "阿富汗", "阿尔及利亚", "阿尔巴尼亚", "阿拉伯联合酋长国", "阿曼", "阿根廷", "阿森松岛", "阿鲁巴",
							"韩国", "香港", "马其顿", "马尔代夫", "马拉维", "马提尼克", "马来西亚", "马约特岛", "马绍尔群岛", "马耳他", "马达加斯加", "马里",
							"黎巴嫩", "黑山"
						]
					}
				},
				"additionalProperties": false
			}
		},
		"batteries_required": {
			"title": "是否需要电池",
			"description": "请说明商品中是否需要电池。",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "需要电池吗？",
						"description": "如果需要使用电池为商品供电，或者如果商品就是电池，请选择“是”；否则，请选择“否”。请注意，内置可充电电池也被视为电池。",
						"editable": true,
						"hidden": false,
						"examples": ["是，没有"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					}
				},
				"additionalProperties": false
			}
		},
		"batteries_included": {
			"title": "是否附带电池",
			"description": "请说明商品中是否附带电池。",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "是否附带电池",
						"description": "请说明商品中是否附带电池。",
						"editable": true,
						"hidden": false,
						"examples": ["是，没有"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					}
				},
				"additionalProperties": false
			}
		},
		"battery": {
			"title": "电池",
			"description": "提供电池信息",
			"examples": ["碱性电池"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"cell_composition": {
						"title": "电池类型",
						"description": "选择电池的化学成分",
						"type": "array",
						"minItems": 1,
						"maxItems": 1,
						"items": {
							"type": "object",
							"required": ["value"],
							"properties": {
								"value": {
									"title": "电池成分",
									"description": "选择电池的化学成分",
									"editable": true,
									"hidden": false,
									"examples": ["镍氢电池，锂离子电池"],
									"type": "string",
									"enum": ["sealed_lead_acid", "mercury_oxide", "aluminum_oxygen", "silver_oxide",
										"zinc_chloride", "wet_alkali", "alkaline", "lithium_phosphate", "polymer",
										"sodium_ion", "lead_acid", "lead_acid_agm", "lead_calcium",
										"silver_calcium", "silver_zinc", "lithium", "lithium_thionyl_chloride",
										"lithium_manganese_dioxide", "lithium_metal", "lithium_ion", "lithium_air",
										"lithium_polymer", "lithium_titanate", "lithium_cobalt",
										"lithium_nickel_cobalt_aluminum", "lithium_nickel_manganese_cobalt", "zinc",
										"zinc_carbon", "zinc_air", "manganese", "NiMh", "nickel_oxyhydroxide",
										"nickel_iron", "nickel_zinc", "NiCAD", "other_than_listed"
									],
									"enumNames": ["密封铅酸", "氧化汞", "氧化铝电池", "氧化银电池", "氯化锌", "湿碱性", "碱性电池", "磷酸锂", "聚合物电池",
										"钠离子", "铅酸电池", "铅酸，AGM", "铅钙电池", "银钙", "银锌", "锂", "锂 - 亚硫酰氯（Li-SOCL2）",
										"锂-二氧化锰电池", "锂电池", "锂离子电池", "锂空气", "锂聚合物电池", "锂钛酸", "锂钴", "锂镍钴铝（NCA）",
										"锂镍锰钴（NMC）", "锌电池", "锌碳", "锌空气", "锰电池", "镍氢电池", "镍氧代氢氧化物", "镍铁", "镍锌",
										"镍镉电池", "除列明的以外"
									]
								}
							},
							"additionalProperties": false
						}
					},
					"cell_composition_other_than_listed": {
						"title": "未列明的电池单元成分",
						"description": "提供未在电池成分中列出的电池单元成分。",
						"examples": ["银氧化物"],
						"type": "array",
						"minItems": 1,
						"minUniqueItems": 1,
						"maxUniqueItems": 1,
						"selectors": ["language_tag"],
						"items": {
							"type": "object",
							"required": ["language_tag", "value"],
							"properties": {
								"language_tag": {
									"$ref": "#/$defs/language_tag"
								},
								"value": {
									"title": "未列明的电池单元成分",
									"description": "提供未在电池成分中列出的电池单元成分。",
									"editable": true,
									"hidden": false,
									"examples": ["银氧化物(Ag2O)"],
									"type": "string",
									"maxLength": 50
								}
							},
							"additionalProperties": false
						}
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"weight": {
						"title": "电池重量",
						"description": "提供包含在内的电池的总净重（数值）。这是不在包装内或未安装在适用设备内的独立电池的重量。",
						"examples": ["2.5克"],
						"type": "array",
						"minItems": 1,
						"maxItems": 1,
						"items": {
							"type": "object",
							"required": ["unit", "value"],
							"properties": {
								"unit": {
									"title": "电池重量单位",
									"description": "提供电池重量单位",
									"editable": true,
									"hidden": false,
									"examples": ["克"],
									"type": "string",
									"enum": ["grams", "kilograms", "tons", "milligrams", "hundredths_pounds", "ounces",
										"pounds"
									],
									"enumNames": ["克", "千克", "吨", "毫克", "百分之一磅", "盎司", "磅"]
								},
								"value": {
									"title": "电池重量",
									"description": "提供包含在内的电池的总净重（数值）。这是不在包装内或未安装在适用设备内的独立电池的重量。",
									"editable": true,
									"hidden": false,
									"examples": ["2.5"],
									"type": "number",
									"minimum": 0,
									"maxLength": 5000
								}
							},
							"additionalProperties": false
						}
					}
				},
				"additionalProperties": false
			}
		},
		"num_batteries": {
			"title": "电池类型",
			"description": "提供商品所需电池的数量和类型。 如果商品内含有电池，请确保对提供的备用电池进行解释",
			"examples": ["1 个 AA、2 个 AAA"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1000,
			"selectors": ["marketplace_id", "type"],
			"items": {
				"type": "object",
				"required": ["quantity", "type"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"quantity": {
						"title": "电池数量",
						"description": "指明给商品供电所需的电池数 如果商品内含有电池，请确保对提供的备用电池进行解释",
						"editable": true,
						"hidden": false,
						"examples": ["1, 4"],
						"type": "integer",
						"minimum": 0
					},
					"type": {
						"title": "电池类型",
						"description": "说明为商品及备件（如有）供电所需的电池类型。某些选项可能会移到其他属性下，如 IEC 代码",
						"editable": true,
						"hidden": false,
						"examples": ["P76"],
						"type": "string",
						"enum": ["12v", "aa", "aaa", "9v", "aaaa", "a", "cr123a", "cr2032", "cr2430", "cr2", "cr5", "c",
							"d", "lr41", "lr43", "lr44", "p76", "product_specific", "unknown", "lithium_ion",
							"lithium_polymer", "lithium_metal", "nonstandard_battery"
						],
						"enumNames": ["12v电池", "5号电池", "7号电池", "9v电池", "9号电池", "a型号", "cr123a型号", "CR2032", "CR2430",
							"cr2型号", "cr5型号", "c型号", "d型号", "LR41", "LR43", "LR44", "p76型号", "产品特定", "未知型号",
							"锂离子电池", "锂聚合物", "锂金属电池", "非标准型号"
						]
					}
				},
				"additionalProperties": false
			}
		},
		"number_of_lithium_metal_cells": {
			"title": "锂金属电池单元数量",
			"description": "商品中独立于电池的锂金属电池单元的总数量。",
			"examples": ["2"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "锂金属电池单元数量",
						"description": "请注明产品中的锂金属电池总数，其中这些电池不在电池壳体中（如纽扣电池）。",
						"editable": true,
						"hidden": false,
						"examples": ["1，3"],
						"type": "integer",
						"minimum": 0
					}
				},
				"additionalProperties": false
			}
		},
		"number_of_lithium_ion_cells": {
			"title": "锂离子电池单元数量",
			"description": "商品中独立于电池的锂离子电池单元的总数量。",
			"examples": ["2"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "锂离子电池单元数量",
						"description": "请注明产品中的锂离子电池总数，其中这些电池不在电池壳体中。例如，AA 电池被视为一块电池",
						"editable": true,
						"hidden": false,
						"examples": ["1，3"],
						"type": "integer",
						"minimum": 0
					}
				},
				"additionalProperties": false
			}
		},
		"lithium_battery": {
			"title": "锂电池",
			"description": "提供锂电池信息",
			"examples": ["毫米"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"energy_content": {
						"title": "锂电池容量",
						"description": "锂电池的容量",
						"examples": ["3"],
						"type": "array",
						"minItems": 1,
						"maxItems": 1,
						"items": {
							"type": "object",
							"required": ["unit", "value"],
							"properties": {
								"unit": {
									"title": "锂电池电量单位",
									"description": "选择相应的单位",
									"editable": false,
									"hidden": false,
									"examples": ["瓦特时"],
									"type": "string",
									"enum": ["kilowatt_hours", "milliampere_hour", "milliamp_hours",
										"milliampere_second", "joules", "watt_hours", "cubic_meters", "cubic_feet",
										"btus"
									],
									"enumNames": ["千瓦时", "毫安培小時", "毫安时", "毫安秒", "焦耳", "瓦時", "立方米", "立方英尺", "英热单位"]
								},
								"value": {
									"title": "锂电池电量",
									"description": "提供锂电池电量（数值）",
									"editable": false,
									"hidden": false,
									"examples": ["4.2"],
									"type": "number",
									"minimum": 0,
									"maxLength": 5000
								}
							},
							"additionalProperties": false
						}
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"packaging": {
						"title": "电池包装类型",
						"description": "电池是独立于该商品，电池是装配在该商品内，电池是包含在该商品的包装内，但没有装配在该商品内",
						"type": "array",
						"minItems": 1,
						"maxItems": 1,
						"items": {
							"type": "object",
							"required": ["value"],
							"properties": {
								"value": {
									"title": "锂电池包装",
									"description": "如果电池在商品内，请选择“在设备内 (in equipment)”。如果电池与其供电的设备是分开包装的，请选择“随附设备 (with equipment)”。如果产品是一款单独出售的电池，或者与其不供电的商品在一起，请选择“唯一 (only)”。",
									"editable": true,
									"hidden": false,
									"examples": ["电池是装配在该商品内，电池是包含在该商品的包装内"],
									"type": "string",
									"enum": ["batteries_packed_with_equipment", "batteries_only",
										"batteries_contained_in_equipment"
									],
									"enumNames": ["电池是包含在该商品的包装内，但没有装配在该商品内", "电池是独立于该商品", "电池是装配在该商品内"]
								}
							},
							"additionalProperties": false
						}
					},
					"weight": {
						"title": "锂电池重量",
						"description": "锂电池的重量。或者在每个电池中，或每组可再充电电池组中，锂含量的重量，以克为单位表示。",
						"examples": ["6"],
						"type": "array",
						"minItems": 1,
						"maxItems": 1,
						"items": {
							"type": "object",
							"required": ["unit", "value"],
							"properties": {
								"unit": {
									"title": "锂电池重量单位",
									"description": "选择相应的重量单位",
									"editable": true,
									"hidden": false,
									"examples": ["克"],
									"type": "string",
									"enum": ["pounds", "grams", "kilograms", "milligrams", "ounces"],
									"enumNames": ["pounds", "克", "千克", "毫克", "盎司"]
								},
								"value": {
									"title": "锂电池重量",
									"description": "提供电池内的锂元素的重量数值",
									"editable": true,
									"hidden": false,
									"examples": ["0.5, 0.03"],
									"type": "number",
									"minimum": 0,
									"maxLength": 12
								}
							},
							"additionalProperties": false
						}
					}
				},
				"additionalProperties": false
			}
		},
		"supplier_declared_dg_hz_regulation": {
			"title": "危险商品规管",
			"description": "如果产品是受运输、储存和/或废物管制的危险商品或危险材料、物质或废物，请从有效值表中进行选择",
			"examples": ["GHS，运输，储存"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1000,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "危险商品规管",
						"description": "如果产品是受运输、储存和/或废物管制的危险商品或危险材料、物质或废物，请从有效值表中进行选择",
						"editable": true,
						"hidden": false,
						"examples": ["GHS，运输，储存"],
						"type": "string",
						"enum": ["ghs", "not_applicable", "storage", "other", "waste", "unknown", "transportation"],
						"enumNames": ["GHS", "不适用", "储存", "其他", "废物", "未知", "运输"]
					}
				},
				"additionalProperties": false
			}
		},
		"ghs": {
			"title": "GHS",
			"description": "提供全球化学品统一分类和标签制度 (GHS) 信息",
			"examples": ["NGK"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"classification": {
						"title": "GHS 分类",
						"description": "提供产品的全球协调系统（GHS）CLP 分类",
						"examples": ["种类：爆炸物，子分类：液体"],
						"type": "array",
						"minItems": 1,
						"minUniqueItems": 1,
						"maxUniqueItems": 1000,
						"selectors": ["class"],
						"items": {
							"type": "object",
							"required": ["class"],
							"properties": {
								"class": {
									"title": "GHS 分类",
									"description": "如果选择 GHS 作为危险品规则，请从有效值列表中选择产品的 GHS 类别。GHS 类别指示由 GHS 分类系统分配的警告声明。",
									"editable": true,
									"hidden": false,
									"examples": ["易爆"],
									"type": "string",
									"enum": ["amzn_specific_no_label_with_warning", "health_hazard", "irritant",
										"compressed_gas", "oxidizing", "flammable", "explosive",
										"environmentally_damaging", "toxic", "corrosive"
									],
									"enumNames": ["Amazon 特定编号带警告标签", "健康危害", "刺激性", "压缩气体", "易氧化", "易燃", "易爆", "有害环境",
										"毒性", "腐蚀性"
									]
								}
							},
							"additionalProperties": false
						}
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"hazmat": {
			"title": "危险品联合国监管ID",
			"description": "根据选择的方面提供与产品相关的危险品信息",
			"examples": ["UN1993"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "aspect"],
			"items": {
				"type": "object",
				"required": ["aspect", "value"],
				"properties": {
					"aspect": {
						"title": "危险品方面",
						"description": "选择危险产品信息的方面或监管机构",
						"editable": false,
						"hidden": false,
						"examples": ["联合国监管身份证"],
						"type": "string",
						"enum": ["united_nations_regulatory_id"],
						"enumNames": ["联合国监管身份证"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "危险品",
						"description": "根据选择的方面提供与产品相关的危险品信息",
						"editable": false,
						"hidden": false,
						"examples": ["UN1993"],
						"type": "string",
						"maxLength": 2197
					}
				},
				"additionalProperties": false
			}
		},
		"safety_data_sheet_url": {
			"title": "安全数据表（SDS 或 MSDS）URL",
			"description": "提供 SDS/MSDS URL。 危险材料 SDS/MSDS 需要，针对潜在危险物质提供物理数据（闪点、pH 等）、健康问题、存储和运输等信息。",
			"examples": ["www.safetysheetsRus.com/hazardous_substance/msds.pdf"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "安全数据表（SDS 或 MSDS）URL",
						"description": "如果在“危险品法规”栏中选择了“不适用”或“未知”以外的任何值，请提供 SDS/MSDS URL",
						"editable": true,
						"hidden": false,
						"examples": ["www.safetysheetsRus.com/hazardous_substance/msds.pdf"],
						"type": "string",
						"maxLength": 23397
					}
				},
				"additionalProperties": false
			}
		},
		"item_weight": {
			"title": "商品重量",
			"description": "商品的重量。",
			"examples": ["30千克、1.5磅"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["unit", "value"],
				"properties": {
					"value": {
						"title": "商品重量",
						"description": "给定商品重量数值（不含包装）",
						"editable": true,
						"hidden": false,
						"examples": ["30、1.5"],
						"type": "number",
						"minimum": 0,
						"maxLength": 5000
					},
					"unit": {
						"title": "商品重量单位",
						"description": "选择相应的单位",
						"editable": true,
						"hidden": false,
						"examples": ["千克、磅"],
						"type": "string",
						"enum": ["grams", "kilograms", "tons", "milligrams", "hundredths_pounds", "ounces", "pounds"],
						"enumNames": ["克", "千克", "吨", "毫克", "百分之一磅", "盎司", "磅"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"directions": {
			"title": "使用说明",
			"description": "如适用，可用于给出商品的使用说明。",
			"examples": ["清洁面部后，轻柔均匀地涂抹适量全效润色面霜"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1000,
			"selectors": ["marketplace_id", "language_tag"],
			"items": {
				"type": "object",
				"required": ["language_tag", "value"],
				"properties": {
					"language_tag": {
						"$ref": "#/$defs/language_tag"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "指南",
						"description": "给定产品使用方式指南",
						"editable": true,
						"hidden": false,
						"examples": [
							"年满 18 岁的成人：该产品每天（即每 24 小时）使用一次，持续 14 天。虽然有些人在 24 小时内症状就能完全缓解，但可能需要 1 到 4 天才能完全起作用。"
						],
						"type": "string",
						"maxUtf8ByteLength": 6000
					}
				},
				"additionalProperties": false
			}
		},
		"california_proposition_65": {
			"title": "加州 65 号提案",
			"description": "提供适用于您的产品的65号提案警告信息（如有）。您可以删除或更改信息，以此证明之前提供的警告信息不再是法律要求的",
			"examples": ["家具；实例"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["compliance_type"],
				"properties": {
					"chemical_names": {
						"title": "加州 65 号提案 化学品名称",
						"description": "如果您选择了食品、家具或化学品警告，您必须指明一种或多种化学品。您证明所提供的化学品符合法律要求，并且只有在法律不再要求时，您才会删除之前提供的化学品。",
						"editable": true,
						"hidden": false,
						"examples": ["铅"],
						"type": "array",
						"minItems": 1,
						"maxItems": 5,
						"items": {
							"type": "string",
							"enum": ["1_1_1_2_tetrachloroethane", "1_1_1_trichloroethane", "1_1_2_2_tetrachloroethane",
								"1_1_dichlor_2_2_bis_p_chlorop_ethyle_dde", "1_1_dichloroethane",
								"1_1_dimethylhydrazine_udmh", "1_2_3_trichloropropane",
								"1_2_dibromo_3_chloropropane_dbcp", "1_2_dichloropropane", "1_2_diethylhydrazine",
								"1_2_dimethylhydrazine", "1_3_butadiene", "1_3_dichloro_2_propanol_1_3_dcp",
								"1_3_dichloropropene", "1_3_dinitropyrene", "1_3_propane_sultone",
								"1_4_butanediol_dimethanesulfona_busulfan", "1_4_dichloro_2_butene", "1_4_dioxane",
								"1_4_dichloro_2_nitrobenzene", "1_6_dinitropyrene", "1_8_dinitropyrene",
								"1_2_chloroethyl_3_4_methylcyclohexyl_1_n",
								"1_2_chloroethyl_3_cyclohexyl_1_nitrosour",
								"1_5_nitrofurfurylidene_amino_2_imidazoli", "1_amino_2_4_dibromoanthraquinone",
								"1_amino_2_methylanthraquinone", "1_bromopropane_1_bp", "1_chloro_4_nitrobenzene",
								"1_hydroxyanthraquinone", "1_naphthylamine", "1_nitropyrene",
								"1_butyl_glycidyl_ether", "1_bromo_3_chloropropane",
								"2_2_bis_bromomethyl_1_3_propanediol", "2_3_7_8_tetrachlorodibenzo_p_dioxin_tcdd",
								"2_3_dibromo_1_propanol", "2_4_5_trimethy_and_its_strong_acid_salts",
								"2_4_6_trichlorophenol", "2_4_6_trinitrotoluene_tnt", "2_4_d_butyric_acid",
								"2_4_diamino_6_chloro_s_triazine_dact", "2_4_diaminoanisole",
								"2_4_diaminoanisole_sulfate", "2_4_diaminotoluene", "2_4_dinitrotoluene",
								"2_4_dichloro_1_nitrobenzene", "2_4_hexadienal_89_percent_trans_trans_is",
								"2_5_hexanedione", "2_6_dimethyl_n_nitrosomorpholine_dmnm", "2_6_dinitrotoluene",
								"2_6_xylidine_2_6_dimethylaniline", "2_2_formylhyd_4_5_nitro_2_furyl_thiazole",
								"2_acetylaminofluorene", "2_amino_5_5_nitro_2_furyl_1_3_4_thiadiaz",
								"2_aminoanthraquinone", "2_aminofluorene", "2_bromopropane_2_bp",
								"2_chloropropionic_acid", "2_methyl_1_nitroanth_of_uncertain_purity",
								"2_methylaziridine_propyleneimine", "2_methylimidazole", "2_naphthylamine",
								"2_nitrofluorene", "2_nitropropane", "2_amino_4_chlorophenol",
								"2_chloronitrobenzene", "infant_boys_ age_29_days_to_24_monthsb",
								"2_mercaptobenzothiazole", "3_3_4_4_tetrachloroazobenzene",
								"3_3_dichloro_4_4_diamino_diphenyl_ether", "3_3_dichlorobenzidine",
								"3_3_dichlorobenzidine_dihydrochloride", "3_3_dimethoxybenzidine_o_dianisidine",
								"3_3_dimethoxybenzidine_dihydrochloride", "3_3_dimethylbenzidine_ortho_tolidine",
								"3_3_dimethylbenzidine_dihydrochloride", "3_7_dinitrofluoranthene",
								"3_9_dinitrofluoranthene", "3_n_nitrosomethylamino_propionitrile",
								"3_amino_9_ethylcarbazole_hydrochloride", "3_chloro_2_methylpropene",
								"3_methylcholanthrene", "3_monochloropropane_1_2_diol_3_mcpd",
								"4_4_diaminodiphen_ether_4_4_oxydianiline", "4_4_methylene_bis_2_chloroaniline",
								"4_4_methylene_bis_2_methylaniline", "4_4_methylene_bis_n_n_dimethyl_benzenami",
								"4_4_methylenedianiline", "4_4_methylenedianiline_dihydrochloride",
								"4_4_thiodianiline", "4_n_nitrosomethyl_1_3_pyridyl_1_butanone",
								"4_amino_2_nitrophenol", "4_aminobiphenyl_4_aminodiphenyl",
								"4_chloro_o_phenylenediamine", "4_dimethylaminoazobenzene", "4_methylimidazole",
								"4_nitrobiphenyl", "4_nitropyrene", "4_vinyl_1_cycloh_diepoxide_vinyl_cyclohe",
								"4_vinylcyclohexene", "morphmethylnitrfurylidenamnooxazolidinon",
								"5_chloro_o_toluidi_its_strong_acid_salts",
								"5_methoxypsor_with_ultraviolet_a_therapy", "5_methylchrysene",
								"5_nitroacenaphthene", "6_nitrochrysene", "7_12_dimethylbenz_a_anthracene",
								"7_h_dibenzo_c_g_carbazole", "8_methoxypsor_with_ultraviolet_a_therapy",
								"a_alpha_c_2_amino_9_h_pyrido_2_3_b_indol", "abiraterone_acetate", "acetaldehyde",
								"acetamide", "acetazolamide", "acetochlor", "acetohydroxamic_acid",
								"acifluorfen_sodium", "acrylamide", "acrylonitrile", "actinomycin_d",
								"af_2_2_2_furyl_3_5_nitro_2_furyl_acrylam", "aflatoxins", "alachlor", "aldrin",
								"all_trans_retinoic_acid", "aloe_vera_non_decolor_whole_leaf_extract", "alprazolam",
								"altretamine", "amantadine_hydrochloride", "amikacin_sulfate", "aminoglutethimide",
								"aminoglycosides", "aminopterin", "amiodarone_hydrochloride", "amitraz", "amitrole",
								"amoxapine", "amsacrine", "anabolic_steroids",
								"analgesic_mixtures_containing_phenacetin", "androstenedione",
								"angiote_converting_enzyme_ace_inhibitors", "aniline", "aniline_hydrochloride",
								"anisindione", "anthraquinone", "antimony_oxide_antimony_trioxide", "aramite",
								"areca_nut", "aristolochic_acids", "arsenic_inorganic_arsenic_compounds",
								"arsenic_inorganic_oxides", "asbestos", "aspirin", "atenolol", "atrazine",
								"auramine", "auranofin", "avermectin_b_1_abamectin", "azacitidine", "azaserine",
								"azathioprine", "azobenzene", "barbiturates", "beclomethasone_dipropionate",
								"benomyl", "benthiavalicarb_isopropyl", "benz_a_anthracene", "benzene",
								"benzidine_and_its_salts", "benzidine_based_dyes", "benzo_a_pyrene",
								"benzo_b_fluoranthene", "benzo_j_fluoranthene", "benzo_k_fluoranthene",
								"benzodiazepines", "benzofuran", "benzophenone", "benzotrichloride",
								"benzphetamine_hydrochloride", "benzyl_chloride", "benzyl_violet_4_b", "beryllium",
								"beryllium_and_beryllium_compounds", "beryllium_oxide", "beryllium_sulfate",
								"beta_butyrolactone", "beta_myrcene", "beta_propiolactone",
								"betel_quid_with_tobacco", "betel_quid_without_tobacco",
								"bis_2_chloro_1_methyleth_ether_technical", "bis_2_chloroethyl_ether",
								"bis_chloromethyl_ether", "bischloroethyl_nitrosourea_bcnu_carmusti",
								"bisphenol_a_bpa", "bitumens_extracts_steam_refined_air_refi", "bracken_fern",
								"bromacil_lithium_salt", "bromate", "bromochloroacetic_acid",
								"bromodichloroacetic_acid", "bromodichloromethane", "bromoethane", "bromoform",
								"bromoxynil", "bromoxynil_octanoate", "butabarbital_sodium",
								"butyl_benzyl_phthalate_bbp_d", "butylated_hydroxyanisole", "c_i_acid_red_114",
								"c_i_basic_red_9_monohydrochloride", "c_i_direct_blue_15", "c_i_direct_blue_218",
								"c_i_disperse_yellow_3", "c_i_solvent_yellow_14", "cacodylic_acid", "cadmium",
								"cadmium_and_cadmium_compounds", "caffeic_acid", "captafol", "captan",
								"carbamazepine", "carbaryl", "carbazole", "carbon_disulfide", "carbon_monoxide",
								"carbon_tetrachloride", "carbon_black_extracts", "carboplatin", "catechol",
								"ceramic_fiber_airbor_part_respirable_siz",
								"certain_combined_chemotherapy_for_lympho", "chenodiol", "chloral",
								"chloral_hydrate", "chlorambucil", "chloramphenicol_sodium_succinate",
								"chlorcyclizine_hydrochloride", "chlordane", "chlordecone_kepone",
								"chlordiazepoxide", "chlordiazepoxide_hydrochloride", "chlordimeform",
								"chlorendic_acid", "chloroethane_ethyl_chloride", "chloroform",
								"chloromethyl_methyl_ether_technical_grad", "chloroprene", "chlorothalonil",
								"chlorotrianisene", "chlorozotocin", "chlorpyrifos",
								"chromium_hexavalent_compounds", "chrysene",
								"ciclosporin_cyclosporin_a_cyclosporine", "cidofovir", "cinnamyl_anthranilate",
								"cisplatin", "citrus_red_no_2", "cladribine", "clarithromycin",
								"clobetasol_propionate", "clofibrate", "clomiphene_citrate",
								"clorazepate_dipotassium", "cmnp_pyrazachlor", "cobalt_ii_oxide",
								"cobalt_metal_powder", "cobalt_sulfate", "cobalt_sulfate_heptahydrate",
								"codeine_phosphate", "coke_oven_emissions", "colchicine", "conjugated_estrogens",
								"creosotes", "cumene", "cupferron", "cyanazine", "cycasin", "cycloate",
								"cycloheximide", "cyclopenta_cd_pyrene", "cyclophosphamide_anhydrous",
								"cyclophosphamide_hydrated", "cyhexatin", "cytarabine", "cytembena",
								"d_c_orange_no_17", "d_c_red_no_19", "d_c_red_no_8", "d_c_red_no_9", "dacarbazine",
								"daminozide", "danazol", "dantron_chrysazin_1_8_dihydroxyanthraqui", "daunomycin",
								"daunorubicin_hydrochloride", "ddd_dichlorodiphenyl_dichloroethane",
								"dde_dichlorodiphenyl_dichloroethylene", "ddt_dichlorodiphenyl_trichloroethane",
								"ddvp_dichlorvos", "demeclocyclin_hydrochloride_internal_use",
								"des_ethyl_atrazine_dea", "des_isopropyl_atrazine_dia",
								"di_2_ethylhexyl_phthalate_dehp", "di_isodecyl_phthalate_didp",
								"di_n_butyl_phthalate_dbp", "di_n_hexyl_phthalate_dn_hp",
								"di_n_propyl_isocinchom_mgk_repellent_326", "diazepam", "diazoaminobenzene",
								"diazoxide", "dibenz_a_c_anthracene", "dibenz_a_h_acridine",
								"dibenz_a_h_anthracene", "dibenz_a_j_acridine", "dibenz_a_j_anthracene",
								"dibenzanthracenes", "dibenzo_a_e_pyrene", "dibenzo_a_h_pyrene",
								"dibenzo_a_i_pyrene", "dibenzo_a_l_pyrene", "dibromoacetic_acid",
								"dibromoacetonitrile", "dichloroacetic_acid", "dichloromethane_methylene_chloride",
								"dichlorophene", "dichlorphenamide", "diclofop_methyl", "dicumarol", "dieldrin",
								"diepoxybutane", "diesel_engine_exhaust", "diethanolamine", "diethyl_sulfate",
								"diethylstilbestrol_des", "diflunisal", "diglycidyl_resorcinol_ether_dgre",
								"dihydroergotamine_mesylate", "dihydrosafrole", "diisononyl_phthalate_dinp",
								"diisopropyl_sulfate", "diltiazem_hydrochloride", "dimethyl_sulfate",
								"dimethylcarbamoyl_chloride", "dimethylvinylchloride",
								"dinitrotoluene_technical_grade", "dinitrotoluene_mixture_2_4_2_6", "dinocap",
								"dinoseb", "diphenylhydantoin_phenytoin", "diphenylhydantoin_phenytoin_sodium_salt",
								"direct_black_38_technical_grade", "direct_blue_6_technical_grade",
								"direct_brown_95_technical_grade", "disodium_cyanodithioimidocarbonate",
								"disperse_blue_1", "diuron", "doxorubicin_hydrochloride_adriamycin",
								"doxycycline_internal_use", "doxycycline_calcium_internal_use",
								"doxycycline_hyclate_internal_use", "doxycycline_monohydrate_internal_use",
								"emissions_from_combustion_of_coal", "endrin", "environmental_tobacco_smoke_ets",
								"epichlorohydrin", "epoxiconazole", "ergotamine_tartrate", "erionite",
								"estradiol_17_b", "estragole", "estrogens_steroidal", "estrone", "estropipate",
								"ethanol_in_alcoholic_beverages", "ethinylestradiol", "ethionamide", "ethoprop",
								"ethyl_acrylate", "ethyl_alcohol_in_alcoholic_beverages",
								"ethyl_dipropylthiocarbamate", "ethyl_methanesulfonate",
								"ethyl_4_4_dichlorobenzilate", "ethylbenzene", "ethylene_dibromide",
								"ethylene_dichloride_1_2_dichloroethane", "ethylene_glycol_ingested",
								"ethylene_glycol_monoethyl_ether", "ethylene_glycol_monoethyl_ether_acetate",
								"ethylene_glycol_monomethyl_ether", "ethylene_glycol_monomethyl_ether_acetate",
								"ethylene_oxide", "ethylene_thiourea", "ethyleneimine_aziridine", "etodolac",
								"etoposide", "etoposide_in_comb_with_cispla_and_bleomy", "etretinate",
								"fenoxaprop_ethyl", "fenoxycarb", "filgrastim", "fluazifop_butyl", "flunisolide",
								"fluorouracil", "fluoxymesterone", "flurazepam_hydrochloride", "flurbiprofen",
								"flutamide", "fluticasone_propionate", "fluvalinate", "folpet", "formaldehyde_gas",
								"fumonisin_b_1", "furan", "furazolidone", "furfuryl_alcohol", "furmecyclox",
								"fusarin_c", "gallium_arsenide", "ganciclovir", "ganciclovir_sodium",
								"gaso_engine_exhaust_condensates_extracts", "gemfibrozil",
								"glass_wool_fibers_inhalable_and_biopersi",
								"glu_p_1_2_amino_6_meth_1_2_a_3_2_d_imida",
								"glu_p_2_2_aminodip_1_2_a_3_2_d_imidazole", "glycidaldehyde", "glycidol",
								"glyphosate", "goldenseal_root_powder", "goserelin_acetate", "griseofulvin",
								"gyromitrin_acetaldehyde_methylformylhydr", "halazepam", "halobetasol_propionate",
								"haloperidol", "halothane", "hc_blue_1", "heptachlor", "heptachlor_epoxide",
								"hexachlorobenzene", "hexachlorobutadiene", "hexachlorocyclohexane_alpha_isomer",
								"hexachlorocyclohexane_beta_isomer", "hexachlorocyclohexane_gamma_isomer",
								"hexachlorocyclohexane_technical_grade", "hexachlorodibenzodioxin",
								"hexachloroethane", "hexafluoroacetone", "hexamethylphosphoramide",
								"histrelin_acetate", "hydramethylnon", "hydrazine", "hydrazine_sulfate",
								"hydrazobenzene_1_2_diphenylhydrazine", "hydrogen_cyanide",
								"hydrogen_cyanide_hcn_and_cyanide_salts", "hydroxyurea", "idarubicin_hydrochloride",
								"ifosfamide", "imazalil", "indeno_1_2_3_cd_pyrene", "indium_phosphide",
								"iodine_131", "iprodione", "iprovalicarb",
								"iq_2_amino_3_methylimida_4_5_f_quinoline", "iron_dextran_complex",
								"isobutyl_nitrite", "isoprene", "isopyrazam", "isotretinoin", "isoxaflutole",
								"kresoxim_methyl", "lactofen", "lasiocarpine", "lead", "lead_acetate",
								"lead_and_lead_compounds", "lead_phosphate", "lead_subacetate", "leather_dust",
								"leuprolide_acetate", "levodopa", "levonorgestrel_implants",
								"lindane_and_other_hexachlorocycl_isomers", "linuron", "lithium_carbonate",
								"lithium_citrate", "lorazepam", "lovastatin", "lynestrenol", "m_dinitrobenzene",
								"malathion", "malonaldehyde_sodium_salt", "mancozeb", "maneb",
								"me_a_alpha_c_amino_methyl_pyrido_indole", "mebendazole",
								"medroxyprogesterone_acetate", "megestrol_acetate",
								"me_iq_2_amino_3_4_dimeth_4_5_f_quinoline",
								"meiqx_amino_dimethylimidazo_quinoxaline", "melphalan", "menotropins",
								"mepanipyrim", "meprobamate", "mercaptopurine", "mercury_and_mercury_compounds",
								"merphalan", "mestranol", "metam_potassium", "methacycline_hydrochloride",
								"metham_sodium", "methanol", "methazole", "methimazole", "methotrexate",
								"methotrexate_sodium", "methyl_bromide_as_a_structural_fumigant",
								"methyl_carbamate", "methyl_chloride", "methyl_iodide", "methyl_isobutyl_ketone",
								"methyl_isobutyl_ketone_mibk", "methyl_isocyanate_mic", "methyl_mercury",
								"methyl_methanesulfonate", "methyl_n_butyl_ketone", "methylazoxymethanol",
								"methylazoxymethanol_acetate", "methyleugenol", "methylhydrazine",
								"methylhydrazine_and_its_salts", "methylhydrazine_sulfate",
								"methylmercury_compounds", "methyltestosterone", "methylthiouracil", "metiram",
								"metronidazole", "michlers_ketone", "midazolam_hydrochloride",
								"minocycline_hydrochloride_internal_use", "mirex", "misoprostol", "mitomycin_c",
								"mitoxantrone_hydrochloride", "molinate", "mon_13900_furilazole",
								"mon_4660_dichl_1_oxa_4_azaspi_4_5_decane", "monocrotaline",
								"mopp_vincrstprednsonnitromstrdprocarbmix", "mustard_gas",
								"mx_3_chloro_4_dich_5_hydr_2_5_h_furanone", "myclobutanil", "n_n_diacetylbenzidine",
								"n_n_bis_2_chloroethyl_2_naphthyla_chlorn", "n_n_dimethyl_p_toluidine",
								"n_n_dimethylacetamide", "n_n_dimethylformamide",
								"NNBISChlorethlNapthylmnChlornapzne", "n_4_5_nitro_2_furyl_2_thiazolyl_acetamid",
								"n_carboxymethyl_n_nitrosourea", "n_hexane", "n_methyl_n_nitro_n_nitrosoguanidine",
								"n_methylolacrylamide", "n_methylpyrrolidone", "n_nitroso_n_ethylurea",
								"n_nitroso_n_methylurea", "n_nitroso_n_methylurethane", "n_nitrosodi_n_butylamine",
								"n_nitrosodi_n_propylamine", "n_nitrosodiethanolamine", "n_nitrosodiethylamine",
								"n_nitrosodimethylamine", "n_nitrosodiphenylamine", "n_nitrosomethyl_n_butylamine",
								"n_nitrosomethyl_n_decylamine", "n_nitrosomethyl_n_dodecylamine",
								"n_nitrosomethyl_n_heptylamine", "n_nitrosomethyl_n_hexylamine",
								"n_nitrosomethyl_n_nonylamine", "n_nitrosomethyl_n_octylamine",
								"n_nitrosomethyl_n_pentylamine", "n_nitrosomethyl_n_propylamine",
								"n_nitrosomethyl_n_tetradecylamine", "n_nitrosomethyl_n_undecylamine",
								"n_nitrosomethylethylamine", "n_nitrosomethylvinylamine", "n_nitrosomorpholine",
								"n_nitrosonornicotine", "n_nitrosopiperidine", "n_nitrosopyrrolidine",
								"n_nitrososarcosine", "nabam", "nafarelin_acetate", "nafenopin", "nalidixic_acid",
								"naphthalene", "neomycin_sulfate_internal_use", "netilmicin_sulfate",
								"nickel_metallic", "nickel_acetate", "nickel_carbonate", "nickel_carbonyl",
								"nickel_compounds", "nickel_hydroxide", "nickel_oxide", "nickel_subsulfide",
								"nickelocene", "nicotine", "nifedipine", "nimodipine", "niridazole", "nitrapyrin",
								"nitrilotriacetic_acid", "nitrilot_acid_trisodium_salt_monohydrate", "nitrobenzene",
								"nitrofen_technical_grade", "nitrofurantoin", "nitrofurazone",
								"nitrogen_mustard_mechlorethamine", "nitrogen_mustard_n_oxide",
								"nitrogen_mustard_n_oxide_hydrochloride", "nitromethane", "nitrous_oxide",
								"norethisterone_norethindrone", "norethis_norethindrone_ethinyl_estradiol",
								"norethisterone_norethindrone_mestranol",
								"norethiste_acetate_norethindrone_acetate", "norethynodrel", "norgestrel",
								"o_p_ddt", "o_aminoazotoluene", "o_anisidine", "o_anisidine_hydrochloride",
								"o_dinitrobenzene", "o_nitroanisole", "o_nitrotoluene", "o_phenylenediamine",
								"o_phenylenediamine_and_its_salts", "o_phenylenediamine_dihydochloride",
								"o_phenylphenate_sodium", "o_phenylphenol", "o_toluidine",
								"o_toluidine_hydrochloride", "o_phenylenediamine dihydrochloride", "ochratoxin_a",
								"oil_orange_ss", "oral_contraceptives_combined", "oral_contraceptives_sequential",
								"oryzalin", "oxadiazon", "oxazepam", "oxydemeton_methyl", "oxymetholone",
								"oxytetracycline_internal_use", "oxytetracycli_hydrochloride_internal_use",
								"oxythioquinox_chinomethionat", "p_p_ddt", "p_a_a_a_tetrachlorotoluene",
								"p_aminoazobenzene", "p_chloro_o_toluidine", "p_chloro_o_toluidine_hydrochloride",
								"p_chloro_o_toluidine_strong_acid_salts", "p_chloroaniline",
								"p_chloroaniline_hydrochloride", "p_cresidine", "p_dichlorobenzene",
								"p_dinitrobenzene", "p_nitrosodiphenylamine", "paclitaxel", "panfuran_s",
								"paramethadione", "parathion", "penicillamine",
								"pent_ether_mixture_de_71_technical_grade", "pentachlorophenol",
								"pentobarbital_sodium", "pentosan_polysulfate_sodium", "pentostatin",
								"perfluorooctane_sulfonate_pfos", "perfluorooctanoic_acid_pfoa", "pertuzumab",
								"phenacemide", "phenacetin", "phenazopyridine", "phenazopyridine_hydrochloride",
								"phenesterin", "phenobarbital", "phenolphthalein", "phenoxybenzamine",
								"phenoxybenzamine_hydrochloride", "phenprocoumon", "phenyl_glycidyl_ether",
								"phenylhydrazine", "phenylhydrazine_and_its_salts", "phenylhydrazine_hydrochloride",
								"phenylphosphine", "phi_p_2_amino_1_methyl_6_phen_4_5_b_pyri", "pimozide",
								"pioglitazone", "pipobroman", "pirimicarb", "plicamycin",
								"polybrominated_biphenyls", "polychlorinated_biphenyls",
								"polychlorinated_dibenzo_p_dioxins", "polychlorinated_dibenzofurans", "polygeenan",
								"ponceau_3_r", "ponceau_mx", "potassium_bromate", "potassium_cyanide",
								"potassium_dimethyldithiocarbamate", "pravastatin_sodium",
								"prednisolone_sodium_phosphate", "primidone", "procarbazine",
								"procarbazine_hydrochloride", "procymidone", "progesterone", "pronamide",
								"propachlor", "propargite", "propazine", "propoxur",
								"propylene_glycol_mono_t_butyl_ether", "propylene_oxide", "propylthiouracil",
								"pulegone", "pymetrozine", "pyridine", "pyrimethamine", "quazepam",
								"quinoline_and_its_strong_acid_salts", "quizalofop_ethyl", "radionuclides",
								"reserpine", "residual_heavy_fuel_oils", "resmethrin", "ribavirin", "riddelliine",
								"rifampin", "s_s_s_tributyl_phosphorotri_tribufos_def", "safrole",
								"salted_fish_chinese_style", "secobarbital_sodium", "sedaxane", "selenium_sulfide",
								"sermorelin_acetate", "shale_oils", "simazine", "sodium_cyanide",
								"sodium_dimethyldithiocarbamate", "sodium_fluoroacetate", "spirodiclofen",
								"spironolactone", "stanozolol", "sterigmatocystin", "streptomycin_sulfate",
								"streptozocin_streptozotocin", "streptozotocin_streptozocin", "styrene",
								"styrene_oxide", "sulfallate", "sulfasalazine_salicylazosulfapyridine",
								"sulfur_dioxidee", "sulindac", "talc_containing_asbestiform_fibers",
								"tamoxifen_and_its_salts", "tamoxifen_citrate", "temazepam", "teniposide",
								"terbacil", "teriparatide", "terrazole", "testosterone_and_its_esters",
								"testosterone_cypionate", "testosterone_enanthate", "tetrabromobisphenol_a",
								"tetrachloroethylene_perchloroethylene", "tetrachlorvinphos",
								"tetracycline_internal_use", "tetracycline_hydrochloride_internal_use",
								"tetracyclines_internal_use", "tetrafluoroethylene", "tetranitromethane",
								"thalidomide", "thioacetamide", "thiodicarb", "thioguanine", "thiophanate_methyl",
								"thiouracil", "thiourea", "thorium_dioxide", "tobacco_smoke",
								"tobacco_smoke_primary", "tobacco_oral_use_of_smokeless_products",
								"tobramycin_sulfate", "toluene", "toluene_diisocyanate", "topiramate",
								"toxaphene_polychlorinated_camphenes", "treosulfan", "triadimefon", "triamterene",
								"triazolam", "tributyltin_methacrylate", "trichlormethine_trimustine_hydrochloride",
								"trichloroacetic_acid", "trichloroethylene", "trientine_hydrochloride", "triforine",
								"trilostane", "trimethadione", "trimethyl_phosphate", "trimetrexate_glucuronate",
								"trim_vx", "triphenyltin_hydroxide", "tris_1_3_dichloro_2_propyl_phospha_tdcpp",
								"tris_1_aziridinyl_phosp_sulfide_thiotepa", "tris_2_3_dibromopropyl_phosphate",
								"tris_2_chloroethyl_phosphate", "trp_p_1_tryptophan_p_1", "trp_p_2_tryptophan_p_2",
								"trypan_blue_commercial_grade", "unleaded_gasoline_wholly_vaporized",
								"uracil_mustard", "urethane_ethyl_carbamate", "urofollitropin",
								"valproate_valproic_acid", "vinblastine_sulfate", "vinclozolin",
								"vincristine_sulfate", "vinyl_bromide", "vinyl_chloride",
								"vinyl_cyclohe_dioxide_4_vinyl_1_cycl_die", "vinyl_fluoride",
								"vinyl_trichloride_1_1_2_trichloroethane",
								"vinylidene_chloride_1_1_dichloroethylene", "vismodegib", "warfarin", "wood_dust",
								"zalcitabine", "zidovudine_azt", "zileuton",
								"alpha_methyl_styrene_alpha_methylstyrene", "molybdenum_trioxide",
								"trimethylolpropane_triacrylate_technical",
								"tris_aziridinyl_p_benzoquinone_triaziquo", "2_ethylhexyl_acrylate",
								"titanium_dioxide_airborne_unbound_partic",
								"vanadiumpentoxide_orthorhcrystallineform",
								"pentachlorophenolandbyproductofsynthesis", "dimethyl_hydrogen_phosphite",
								"3_3_dimethylbenzidine_based_dyes_metabol",
								"3_3_dimethoxybenzidine_based_dyes_metabo", "perfluorononanoicacid_pfna_salts",
								"perfluorooctanesulfonicacid _pfos_salts", "n_nitrosohexamethyleneimine",
								"neonatal_infant_boys_age_0_to_28_daysb", "bis_2chloro1methylethyl_ethertechgrade",
								"bisphenol_s_bps", "trans_2_dimethylamino_methylimino_5_2_5_",
								"strong_inorganic_acid_mists_containing_s",
								"herbal_remedies_containing_plant_species", "tetrahydrofuran",
								"cyanidesaltsdisassociatesolution", "polychlorinatedbiphenyls60ormoreperent",
								"p_chloro_alpha_alpha_alpha_trifluorotolu", "para_nitroanisole", "adultb",
								"leucomalachite_green", "toxins_derived_from_fusarium_moniliforme",
								"coconutoilcocamidediethanolamine", "fluoro_edenite_fibrous_amphibole",
								"indium_tin_oxide", "vinylidenechloride",
								"chlorinated_paraffins_average_chain_leng", "hydrogen_cyanidef", "sodium_cyanidef",
								"potassium_cyanidef", "carbon_black_airborne_unbound_particles_",
								"soots_tars_and_mineral_oils_untreated_an", "coal_tar_pitch",
								"glycidyl_methacrylate", "methyl_acrylate",
								"nitromustardhydrochloridemechlorethamine", "silicon_carbide_whiskers",
								"silica_crystalline_airborne_particles_of", "anthracene",
								"angiotensinconvertenzyme _ace_inhibitors",
								"retinol_retinyl_esters_when_in_daily_dos", "bevacizumab", "alcoholic_beverages",
								"alcoholicbeverage_associatedalcoholabuse",
								"nickelrefinerydustpyrometallurgprocess", "nickel_soluble_compounds",
								"palygorskite_fibers_morthn_5µm_length", "estrogen_progestogen_combined_used_as_me",
								"emissions_from_high_temperature_unrefine", "gentian_violet_crystal_violet"
							],
							"enumNames": ["1,1,1,2-Tetrachloroethane", "1,1,1-三氯乙烷", "1,1,2,2-Tetrachloroethane",
								"1,1-Dichloro-2,2-bis(p-chloropheny)ethylene (DDE)", "1,1-Dichloroethane",
								"1,1-Dimethylhydrazine (UDMH)", "1,2,3-Trichloropropane",
								"1,2-Dibromo-3-chloropropane (DBCP)", "1,2-Dichloropropane", "1,2-Diethylhydrazine",
								"1,2-Dimethylhydrazine", "1,3-Butadiene", "1,3-Dichloro-2-propanol (1,3-DCP)",
								"1,3-Dichloropropene", "1,3-Dinitropyrene", "1,3-Propane sultone",
								"1,4-Butanediol dimethanesulfonate (Busulfan)", "1,4-Dichloro-2-butene",
								"1,4-Dioxane", "1,4-二氯-2-硝基苯", "1,6-Dinitropyrene", "1,8-Dinitropyrene",
								"1-(2-氯乙基) -3-(4-甲基环己基) -1-亚硝基脲盐 (甲基-CCNU)",
								"1-(2-氯乙基) -3-环己基-1-亚硝基脲类 (CCNU) (洛莫司汀)",
								"1-[(5-Nitrofurfurylidene)-amino]-2-imidazolidinone",
								"1-Amino-2,4-dibromoanthraquinone", "1-Amino-2-methylanthraquinone",
								"1-Bromopropane (1-BP)", "1-Chloro-4-nitrobenzene", "1-Hydroxyanthraquinone",
								"1-Naphthylamine", "1-Nitropyrene", "1-丁基环氧乙烷", "1-溴-3-氯丙烷",
								"2,2-Bis(bromomethyl)-1,3-propanediol",
								"2,3,7,8-Tetrachlorodibenzo-p-dioxin (TCDD)", "2,3-Dibromo-1-propanol",
								"2,4,5-Trimethylaniline and its strong acid salts", "2,4,6-Trichlorophenol",
								"2,4,6-Trinitrotoluene (TNT)", "2,4-D butyric acid",
								"2,4-Diamino-6-chloro-s-triazine (DACT)", "2,4-Diaminoanisole",
								"2,4-Diaminoanisole sulfate", "2,4-Diaminotoluene", "2,4-Dinitrotoluene",
								"2,4-二氯-1-硝基苯", "2,4-己二烯醛（89% 反式，反式异构体；11% 顺式，反式异构体）", "2,5-Hexanedione",
								"2,6-Dimethyl-N-nitrosomorpholine (DMNM)", "2,6-Dinitrotoluene",
								"2,6-Xylidine (2,6-Dimethylaniline)",
								"2-(2-Formylhydrazino)-4-(5-nitro-2-furyl)thiazole", "2-Acetylaminofluorene",
								"2-Amino-5-(5-nitro-2-furyl)-1,3,4-thiadiazole", "2-Aminoanthraquinone",
								"2-Aminofluorene", "2-Bromopropane (2-BP)", "2-Chloropropionic acid",
								"2-Methyl-1-nitroanthraquinone (of uncertain purity)",
								"2-Methylaziridine (Propyleneimine)", "2-Methylimidazole", "2-Naphthylamine",
								"2-Nitrofluorene", "2-Nitropropane", "2-氨基-4-氯苯酚", "2-氯硝基苯", "29天至24个月大的婴儿男孩",
								"2‑Mercaptobenzothiazole", "3,3',4,4'-Tetrachloroazobenzene",
								"3,3'-Dichloro-4,4'-diamino-diphenyl ether", "3,3'-Dichlorobenzidine",
								"3,3'-Dichlorobenzidine dihydrochloride", "3,3'-Dimethoxybenzidine (o-Dianisidine)",
								"3,3'-Dimethoxybenzidine dihydrochloride",
								"3,3'-Dimethylbenzidine (ortho-Tolidine)", "3,3'-Dimethylbenzidine dihydrochloride",
								"3,7-Dinitrofluoranthene", "3,9-Dinitrofluoranthene",
								"3-(N-Nitrosomethylamino) propionitrile", "3-Amino-9-ethylcarbazole hydrochloride",
								"3-Chloro-2-methylpropene", "3-Methylcholanthrene",
								"3-Monochloropropane-1,2-diol (3-MCPD)",
								"4,4'-Diaminodiphenyl ether (4,4'-Oxydianiline)",
								"4,4'-Methylene bis(2-chloroaniline)", "4,4'-Methylene bis(2-methylaniline)",
								"4,4'-Methylene bis(N,N-dimethyl)benzenamine", "4,4'-Methylenedianiline",
								"4,4'-Methylenedianiline dihydrochloride", "4,4'-Thiodianiline",
								"4-(N-Nitrosomethylamino)-1-(3-pyridyl)1-butanone", "4-Amino-2-nitrophenol",
								"4-Aminobiphenyl (4-aminodiphenyl)", "4-Chloro-o-phenylenediamine",
								"4-Dimethylaminoazobenzene", "4-Methylimidazole", "4-Nitrobiphenyl",
								"4-Nitropyrene", "4-Vinyl-1-cyclohexene diepoxide (Vinyl cyclohexenedioxide)",
								"4-Vinylcyclohexene", "5-(吗啉甲基)-3-(5-硝基糠醛缩氨基)-2-恶唑烷酮",
								"5-Chloro-o-toluidine and its strong acid salts",
								"5-Methoxypsoralen with ultraviolet A therapy", "5-Methylchrysene",
								"5-Nitroacenaphthene", "6-Nitrochrysene", "7,12-Dimethylbenz(a)anthracene",
								"7H-Dibenzo[c,g]carbazole", "8-Methoxypsoralen with ultraviolet A therapy",
								"A-alpha-C (2-Amino-9H-pyrido[2,3-b]indole)", "Abiraterone acetate", "Acetaldehyde",
								"Acetamide", "Acetazolamide", "Acetochlor", "Acetohydroxamic acid",
								"Acifluorfen sodium", "Acrylamide", "Acrylonitrile", "Actinomycin D",
								"AF-2;[2-(2-furyl)-3-(5-nitro-2-furyl)]acrylamide", "Aflatoxins", "Alachlor",
								"Aldrin", "All-trans retinoic acid",
								"Aloe Vera, non-decolorized whole leaf extract", "Alprazolam", "Altretamine",
								"Amantadine hydrochloride", "Amikacin sulfate", "Aminoglutethimide",
								"Aminoglycosides", "Aminopterin", "Amiodarone hydrochloride", "Amitraz", "Amitrole",
								"Amoxapine", "Amsacrine", "Anabolic steroids",
								"Analgesic mixtures containing Phenacetin", "Androstenedione",
								"Angiotensin converting enzyme (ACE) inhibitors", "Aniline",
								"Aniline hydrochloride", "Anisindione", "Anthraquinone",
								"Antimony oxide (Antimony trioxide)", "Aramite", "Areca nut", "Aristolochic acids",
								"Arsenic (inorganic arsenic compounds)", "Arsenic (inorganic oxides)", "Asbestos",
								"Aspirin", "Atenolol", "Atrazine", "Auramine", "Auranofin",
								"Avermectin B1 (Abamectin)", "Azacitidine", "Azaserine", "Azathioprine",
								"Azobenzene", "Barbiturates", "Beclomethasone dipropionate", "Benomyl",
								"Benthiavalicarb-isopropyl", "Benz[a]anthracene", "Benzene",
								"Benzidine [and its salts]", "Benzidine-based dyes", "Benzo[a]pyrene",
								"Benzo[b]fluoranthene", "Benzo[j]fluoranthene", "Benzo[k]fluoranthene",
								"Benzodiazepines", "Benzofuran", "Benzophenone", "Benzotrichloride",
								"Benzphetamine hydrochloride", "Benzyl chloride", "Benzyl violet 4B", "Beryllium",
								"Beryllium and beryllium compounds", "Beryllium oxide", "Beryllium sulfate",
								"beta-Butyrolactone", "beta-Myrcene", "beta-Propiolactone",
								"Betel quid with tobacco", "Betel quid without tobacco",
								"Bis(2-chloro-1-methylethyl)ether, technical grade", "Bis(2-chloroethyl)ether",
								"Bis(chloromethyl)ether", "Bischloroethyl nitrosourea (BCNU) (Carmustine)",
								"Bisphenol A (BPA)", "Bitumens, extracts of steam-refined and air refined",
								"Bracken fern", "Bromacil lithium salt", "Bromate", "Bromochloroacetic acid",
								"Bromodichloroacetic acid", "Bromodichloromethane", "Bromoethane", "Bromoform",
								"Bromoxynil", "Bromoxynil octanoate", "Butabarbital sodium",
								"Butyl benzyl phthalate (BBP)d", "Butylated hydroxyanisole", "C.I. Acid Red 114",
								"C.I. Basic Red 9 monohydrochloride", "C.I. Direct Blue 15", "C.I. Direct Blue 218",
								"C.I. Disperse Yellow 3", "C.I. Solvent Yellow 14", "Cacodylic acid", "Cadmium",
								"Cadmium and cadmium compounds", "Caffeic acid", "Captafol", "Captan",
								"Carbamazepine", "Carbaryl", "Carbazole", "Carbon disulfide", "Carbon monoxide",
								"Carbon tetrachloride", "Carbon-black extracts", "Carboplatin", "Catechol",
								"Ceramic fibers (airborne particles of respirable size)",
								"Certain combined chemotherapy for lymphomas", "Chenodiol", "Chloral",
								"Chloral hydrate", "Chlorambucil", "Chloramphenicol sodium succinate",
								"Chlorcyclizine hydrochloride", "Chlordane", "Chlordecone (Kepone)",
								"Chlordiazepoxide", "Chlordiazepoxide hydrochloride", "Chlordimeform",
								"Chlorendic acid", "Chloroethane (Ethyl chloride)", "Chloroform",
								"Chloromethyl methyl ether (technical grade)", "Chloroprene", "Chlorothalonil",
								"Chlorotrianisene", "Chlorozotocin", "Chlorpyrifos",
								"Chromium (hexavalent compounds)", "Chrysene",
								"Ciclosporin (Cyclosporin A; Cyclosporine)", "Cidofovir", "Cinnamyl anthranilate",
								"Cisplatin", "Citrus Red No. 2", "Cladribine", "Clarithromycin",
								"Clobetasol propionate", "Clofibrate", "Clomiphene citrate",
								"Clorazepate dipotassium", "CMNP (pyrazachlor)", "Cobalt [II] oxide",
								"Cobalt metal powder", "Cobalt sulfate", "Cobalt sulfate heptahydrate",
								"Codeine phosphate", "Coke oven emissions", "Colchicine", "Conjugated estrogens",
								"Creosotes", "Cumene", "Cupferron", "Cyanazine", "Cycasin", "Cycloate",
								"Cycloheximide", "Cyclopenta[cd]pyrene", "Cyclophosphamide (anhydrous)",
								"Cyclophosphamide (hydrated)", "Cyhexatin", "Cytarabine", "Cytembena",
								"D&C Orange No. 17", "D&C Red No. 19", "D&C Red No. 8", "D&C Red No. 9",
								"Dacarbazine", "Daminozide", "Danazol",
								"Dantron (Chrysazin; 1,8-Dihydroxyanthraquinone)", "Daunomycin",
								"Daunorubicin hydrochloride", "DDD (Dichlorodiphenyl-dichloroethane)",
								"DDE (Dichlorodiphenyl-dichloroethylene)", "DDT (Dichlorodiphenyl-trichloroethane)",
								"DDVP (Dichlorvos)", "Demeclocycline hydrochloride (internal use)",
								"Des-ethyl atrazine (DEA)", "Des-isopropyl atrazine (DIA)",
								"Di(2-ethylhexyl)phthalate (DEHP)", "Di-isodecyl phthalate (DIDP)",
								"Di-n-butyl phthalate (DBP)", "Di-n-hexyl phthalate (DnHP)",
								"Di-n-propyl isocinchomeronate (MGK Repellent 326)", "Diazepam",
								"Diazoaminobenzene", "Diazoxide", "Dibenz[a,c]anthracene", "Dibenz[a,h]acridine",
								"Dibenz[a,h]anthracene", "Dibenz[a,j]acridine", "Dibenz[a,j]anthracene",
								"Dibenzanthracenes", "Dibenzo[a,e]pyrene", "Dibenzo[a,h]pyrene",
								"Dibenzo[a,i]pyrene", "Dibenzo[a,l]pyrene", "Dibromoacetic acid",
								"Dibromoacetonitrile", "Dichloroacetic acid",
								"Dichloromethane (Methylene chloride)", "Dichlorophene", "Dichlorphenamide",
								"Diclofop-methyl", "Dicumarol", "Dieldrin", "Diepoxybutane",
								"Diesel engine exhaust", "Diethanolamine", "Diethyl sulfate",
								"Diethylstilbestrol (DES)", "Diflunisal", "Diglycidyl resorcinol ether (DGRE)",
								"Dihydroergotamine mesylate", "Dihydrosafrole", "Diisononyl phthalate (DINP)",
								"Diisopropyl sulfate", "Diltiazem hydrochloride", "Dimethyl sulfate",
								"Dimethylcarbamoyl chloride", "Dimethylvinylchloride",
								"Dinitrotoluene (technical grade)", "Dinitrotoluene mixture, 2,4-/2,6-", "Dinocap",
								"Dinoseb", "Diphenylhydantoin (Phenytoin)",
								"Diphenylhydantoin (Phenytoin), sodium salt", "Direct Black 38 (technical grade)",
								"Direct Blue 6 (technical grade)", "Direct Brown 95 (technical grade)",
								"Disodium cyanodithioimidocarbonate", "Disperse Blue 1", "Diuron",
								"Doxorubicin hydrochloride (Adriamycin)", "Doxycycline (internal use)",
								"Doxycycline calcium (internal use)", "Doxycycline hyclate (internal use)",
								"Doxycycline monohydrate (internal use)", "Emissions from combustion of coal",
								"Endrin", "Environmental tobacco smoke (ETS)", "Epichlorohydrin", "Epoxiconazole",
								"Ergotamine tartrate", "Erionite", "Estradiol 17B", "Estragole",
								"Estrogens, steroidal", "Estrone", "Estropipate", "Ethanol in alcoholic beverages",
								"Ethinylestradiol", "Ethionamide", "Ethoprop", "Ethyl acrylate",
								"Ethyl alcohol in alcoholic beverages", "Ethyl dipropylthiocarbamate",
								"Ethyl methanesulfonate", "Ethyl-4,4'-dichlorobenzilate", "Ethylbenzene",
								"Ethylene dibromide", "Ethylene dichloride (1,2-Dichloroethane)",
								"Ethylene glycol (ingested)", "Ethylene glycol monoethyl ether",
								"Ethylene glycol monoethyl ether acetate", "Ethylene glycol monomethyl ether",
								"Ethylene glycol monomethyl ether acetate", "Ethylene oxide", "Ethylene thiourea",
								"Ethyleneimine (Aziridine)", "Etodolac", "Etoposide",
								"Etoposide in combination with cisplatin and bleomycin", "Etretinate",
								"Fenoxaprop ethyl", "Fenoxycarb", "Filgrastim", "Fluazifop butyl", "Flunisolide",
								"Fluorouracil", "Fluoxymesterone", "Flurazepam hydrochloride", "Flurbiprofen",
								"Flutamide", "Fluticasone propionate", "Fluvalinate", "Folpet",
								"Formaldehyde (gas)", "Fumonisin B1", "Furan", "Furazolidone", "Furfuryl alcohol",
								"Furmecyclox", "Fusarin C", "Gallium arsenide", "Ganciclovir", "Ganciclovir sodium",
								"Gasoline engine exhaust (condensates/extracts)", "Gemfibrozil",
								"Glass wool fibers (inhalable and biopersistent)",
								"Glu-P-1 (2-Amino-6-methyldipyrido[1,2- a:3',2'-d]imidazole)",
								"Glu-P-2 (2-Aminodipyrido[1,2-a:3',2'-d]imidazole)", "Glycidaldehyde", "Glycidol",
								"Glyphosate", "Goldenseal root powder", "Goserelin acetate", "Griseofulvin",
								"Gyromitrin (Acetaldehyde methylformylhydrazone)", "Halazepam",
								"Halobetasol propionate", "Haloperidol", "Halothane", "HC Blue 1", "Heptachlor",
								"Heptachlor epoxide", "Hexachlorobenzene", "Hexachlorobutadiene",
								"Hexachlorocyclohexane (alpha isomer)", "Hexachlorocyclohexane (beta isomer)",
								"Hexachlorocyclohexane (gamma isomer)", "Hexachlorocyclohexane (technical grade)",
								"Hexachlorodibenzodioxin", "Hexachloroethane", "Hexafluoroacetone",
								"Hexamethylphosphoramide", "Histrelin acetate", "Hydramethylnon", "Hydrazine",
								"Hydrazine sulfate", "Hydrazobenzene (1,2-Diphenylhydrazine)", "Hydrogen cyanide",
								"Hydrogen cyanide (HCN) and cyanide salts (CN salts)", "Hydroxyurea",
								"Idarubicin hydrochloride", "Ifosfamide", "Imazalil", "Indeno[1,2,3-cd]pyrene",
								"Indium phosphide", "Iodine-131", "Iprodione", "Iprovalicarb",
								"IQ (2-Amino-3-methylimidazo[4,5-f] quinoline)", "Iron dextran complex",
								"Isobutyl nitrite", "Isoprene", "Isopyrazam", "Isotretinoin", "Isoxaflutole",
								"Kresoxim-methyl", "Lactofen", "Lasiocarpine", "Lead", "Lead acetate",
								"Lead and lead compounds", "Lead phosphate", "Lead subacetate", "Leather dust",
								"Leuprolide acetate", "Levodopa", "Levonorgestrel implants",
								"Lindane and other hexachlorocyclohexane isomers", "Linuron", "Lithium carbonate",
								"Lithium citrate", "Lorazepam", "Lovastatin", "Lynestrenol", "m-Dinitrobenzene",
								"Malathion", "Malonaldehyde, sodium salt", "Mancozeb", "Maneb",
								"Me-A-alpha-C (2-氨基-3-甲基-9H-吡啶并[2,3-b]吲哚)", "Mebendazole",
								"Medroxyprogesterone acetate", "Megestrol acetate",
								"MeIQ (2-Amino-3,4-dimethylimidazo[4,5-f]quinoline)",
								"MeIQx(2-氨基-3,8-二甲基咪唑并[4,5-f]喹啉)", "Melphalan", "Menotropins", "Mepanipyrim",
								"Meprobamate", "Mercaptopurine", "Mercury and mercury compounds", "Merphalan",
								"Mestranol", "Metam potassium", "Methacycline hydrochloride", "Metham sodium",
								"Methanol", "Methazole", "Methimazole", "Methotrexate", "Methotrexate sodium",
								"Methyl bromide, as a structural fumigant", "Methyl carbamate", "Methyl chloride",
								"Methyl iodide", "Methyl isobutyl ketone", "Methyl isobutyl ketone (MIBK)",
								"Methyl isocyanate (MIC)", "Methyl mercury", "Methyl methanesulfonate",
								"Methyl-n-butyl ketone", "Methylazoxymethanol", "Methylazoxymethanol acetate",
								"Methyleugenol", "Methylhydrazine", "Methylhydrazine and its salts",
								"Methylhydrazine sulfate", "Methylmercury compounds", "Methyltestosterone",
								"Methylthiouracil", "Metiram", "Metronidazole", "Michler's ketone",
								"Midazolam hydrochloride", "Minocycline hydrochloride (internal use)", "Mirex",
								"Misoprostol", "Mitomycin C", "Mitoxantrone hydrochloride", "Molinate",
								"MON 13900 (furilazole)", "MON 4660 (dichloroacetyl-1-oxa-4-azaspiro(4,5)-decane",
								"Monocrotaline", "MOPP（长春新碱-泼尼松-氮芥-甲基苄肼混合物）", "Mustard Gas",
								"MX (3-chloro-4-dichloromethyl-5-hydroxy-2(5H)-furanone)", "Myclobutanil",
								"N,N'-Diacetylbenzidine", "N,N-Bis(2-chloroethyl)-2-naphthylamine (Chlornapazine)",
								"N,N-Dimethyl-p-toluidine", "N,N-Dimethylacetamide", "N,N-Dimethylformamide",
								"N,N-双(2-氯乙基)-2-萘胺（萘氮芥）", "N-[4-(5-Nitro-2-furyl)-2-thiazolyl]acetamide",
								"N-Carboxymethyl-N-nitrosourea", "n-Hexane", "N-Methyl-N'-nitro-N-nitrosoguanidine",
								"N-Methylolacrylamide", "N-Methylpyrrolidone", "N-Nitroso-N-ethylurea",
								"N-Nitroso-N-methylurea", "N-Nitroso-N-methylurethane", "N-Nitrosodi-n-butylamine",
								"N-Nitrosodi-n-propylamine", "N-Nitrosodiethanolamine", "N-Nitrosodiethylamine",
								"N-Nitrosodimethylamine", "N-Nitrosodiphenylamine", "N-Nitrosomethyl-n-butylamine",
								"N-Nitrosomethyl-n-decylamine", "N-Nitrosomethyl-n-dodecylamine",
								"N-Nitrosomethyl-n-heptylamine", "N-Nitrosomethyl-n-hexylamine",
								"N-Nitrosomethyl-n-nonylamine", "N-Nitrosomethyl-n-octylamine",
								"N-Nitrosomethyl-n-pentylamine", "N-Nitrosomethyl-n-propylamine",
								"N-Nitrosomethyl-n-tetradecylamine", "N-Nitrosomethyl-n-undecylamine",
								"N-Nitrosomethylethylamine", "N-Nitrosomethylvinylamine", "N-Nitrosomorpholine",
								"N-Nitrosonornicotine", "N-Nitrosopiperidine", "N-Nitrosopyrrolidine",
								"N-Nitrososarcosine", "Nabam", "Nafarelin acetate", "Nafenopin", "Nalidixic acid",
								"Naphthalene", "Neomycin sulfate (internal use)", "Netilmicin sulfate",
								"Nickel (Metallic)", "Nickel acetate", "Nickel carbonate", "Nickel carbonyl",
								"Nickel compounds", "Nickel hydroxide", "Nickel oxide", "Nickel subsulfide",
								"Nickelocene", "Nicotine", "Nifedipine", "Nimodipine", "Niridazole", "Nitrapyrin",
								"Nitrilotriacetic acid", "Nitrilotriacetic acid, trisodium salt monohydrate",
								"Nitrobenzene", "Nitrofen (technical grade)", "Nitrofurantoin", "Nitrofurazone",
								"Nitrogen mustard (Mechlorethamine)", "Nitrogen mustard N-oxide",
								"Nitrogen mustard N-oxide hydrochloride", "Nitromethane", "Nitrous oxide",
								"Norethisterone (Norethindrone)",
								"Norethisterone (Norethindrone) /Ethinyl estradiol",
								"Norethisterone (Norethindrone) /Mestranol",
								"Norethisterone acetate (Norethindrone acetate)", "Norethynodrel", "Norgestrel",
								"o,p'-DDT", "o-Aminoazotoluene", "o-Anisidine", "o-Anisidine hydrochloride",
								"o-Dinitrobenzene", "o-Nitroanisole", "o-Nitrotoluene", "o-Phenylenediamine",
								"o-Phenylenediamine and its salts", "o-Phenylenediamine dihydochloride",
								"o-Phenylphenate, sodium", "o-Phenylphenol", "o-Toluidine",
								"o-Toluidine hydrochloride", "o-苯二胺二盐酸盐类", "Ochratoxin A", "Oil Orange SS",
								"Oral contraceptives, combined", "Oral contraceptives, sequential", "Oryzalin",
								"Oxadiazon", "Oxazepam", "Oxydemeton methyl", "Oxymetholone",
								"Oxytetracycline (internal use)", "Oxytetracycline hydrochloride (internal use)",
								"Oxythioquinox (Chinomethionat)", "p,p'-DDT", "p-a,a,a-Tetrachlorotoluene",
								"p-Aminoazobenzene", "p-Chloro-o-toluidine", "p-Chloro-o-toluidine, hydrochloride",
								"p-Chloro-o-toluidine, strong acid salts of", "p-Chloroaniline",
								"p-Chloroaniline hydrochloride", "p-Cresidine", "p-Dichlorobenzene",
								"p-Dinitrobenzene", "p-Nitrosodiphenylamine", "Paclitaxel", "Panfuran S",
								"Paramethadione", "Parathion", "Penicillamine",
								"pentabromodiphenyl ether mixture [DE-71 (technical grade)]", "Pentachlorophenol",
								"Pentobarbital sodium", "Pentosan polysulfate sodium", "Pentostatin",
								"Perfluorooctane sulfonate (PFOS)", "Perfluorooctanoic acid (PFOA)", "Pertuzumab",
								"Phenacemide", "Phenacetin", "Phenazopyridine", "Phenazopyridine hydrochloride",
								"Phenesterin", "Phenobarbital", "Phenolphthalein", "Phenoxybenzamine",
								"Phenoxybenzamine hydrochloride", "Phenprocoumon", "Phenyl glycidyl ether",
								"Phenylhydrazine", "Phenylhydrazine and its salts", "Phenylhydrazine hydrochloride",
								"Phenylphosphine", "PhiP(2-Amino-1-methyl-6-phenylimidazol[4,5-b]pyridine)",
								"Pimozide", "Pioglitazone", "Pipobroman", "Pirimicarb", "Plicamycin",
								"Polybrominated biphenyls", "Polychlorinated biphenyls",
								"Polychlorinated dibenzo-p-dioxins", "Polychlorinated dibenzofurans", "Polygeenan",
								"Ponceau 3R", "Ponceau MX", "Potassium bromate", "Potassium cyanide",
								"Potassium dimethyldithiocarbamate", "Pravastatin sodium",
								"Prednisolone sodium phosphate", "Primidone", "Procarbazine",
								"Procarbazine hydrochloride", "Procymidone", "Progesterone", "Pronamide",
								"Propachlor", "Propargite", "Propazine", "Propoxur",
								"Propylene glycol mono-t-butyl ether", "Propylene oxide", "Propylthiouracil",
								"Pulegone", "Pymetrozine", "Pyridine", "Pyrimethamine", "Quazepam",
								"Quinoline and its strong acid salts", "Quizalofop-ethyl", "Radionuclides",
								"Reserpine", "Residual (heavy) fuel oils", "Resmethrin", "Ribavirin", "Riddelliine",
								"Rifampin", "S,S,S-Tributyl phosphorotrithioate (Tribufos, DEF)", "Safrole",
								"Salted fish, Chinese-style", "Secobarbital sodium", "Sedaxane", "Selenium sulfide",
								"Sermorelin acetate", "Shale-oils", "Simazine", "Sodium cyanide",
								"Sodium dimethyldithiocarbamate", "Sodium fluoroacetate", "Spirodiclofen",
								"Spironolactone", "Stanozolol", "Sterigmatocystin", "Streptomycin sulfate",
								"Streptozocin (streptozotocin)", "Streptozotocin (streptozocin)", "Styrene",
								"Styrene oxide", "Sulfallate", "Sulfasalazine (Salicylazosulfapyridine)",
								"Sulfur dioxidee", "Sulindac", "Talc containing asbestiform fibers",
								"Tamoxifen and its salts", "Tamoxifen citrate", "Temazepam", "Teniposide",
								"Terbacil", "Teriparatide", "Terrazole", "Testosterone and its esters",
								"Testosterone cypionate", "Testosterone enanthate", "Tetrabromobisphenol A",
								"Tetrachloroethylene (Perchloroethylene)", "Tetrachlorvinphos",
								"Tetracycline (internal use)", "Tetracycline hydrochloride (internal use)",
								"Tetracyclines (internal use)", "Tetrafluoroethylene", "Tetranitromethane",
								"Thalidomide", "Thioacetamide", "Thiodicarb", "Thioguanine", "Thiophanate methyl",
								"Thiouracil", "Thiourea", "Thorium dioxide", "Tobacco smoke",
								"Tobacco smoke (primary)", "Tobacco, oral use of smokeless products",
								"Tobramycin sulfate", "Toluene", "Toluene diisocyanate", "Topiramate",
								"Toxaphene (Polychlorinated camphenes)", "Treosulfan", "Triadimefon", "Triamterene",
								"Triazolam", "Tributyltin methacrylate",
								"Trichlormethine (Trimustine hydrochloride)", "Trichloroacetic acid",
								"Trichloroethylene", "Trientine hydrochloride", "Triforine", "Trilostane",
								"Trimethadione", "Trimethyl phosphate", "Trimetrexate glucuronate", "TRIM® VX",
								"Triphenyltin hydroxide", "Tris(1,3-dichloro-2-propyl) phosphate (TDCPP)",
								"Tris(1-aziridinyl)phosphine sulfide (Thiotepa)",
								"Tris(2,3-dibromopropyl)phosphate", "Tris(2-chloroethyl) phosphate",
								"Trp-P-1 (Tryptophan-P-1)", "Trp-P-2 (Tryptophan-P-2)",
								"Trypan blue (commercial grade)", "Unleaded gasoline (wholly vaporized)",
								"Uracil mustard", "Urethane (Ethyl carbamate)", "Urofollitropin",
								"Valproate (Valproic acid)", "Vinblastine sulfate", "Vinclozolin",
								"Vincristine sulfate", "Vinyl bromide", "Vinyl chloride",
								"Vinyl cyclohexene dioxide (4-Vinyl-1-cyclohexene diepoxide)", "Vinyl fluoride",
								"Vinyl trichloride (1,1,2-Trichloroethane)",
								"Vinylidene chloride (1,1-Dichloroethylene)", "Vismodegib", "Warfarin", "Wood dust",
								"Zalcitabine", "Zidovudine (AZT)", "Zileuton",
								"α-Methyl styrene (alpha-Methylstyrene)", "三氧化钼", "三羟甲基丙烷三丙烯酸酯，工业级",
								"三（氮丙啶基）-对苯醌（三氮醌）", "丙烯酸2-乙基己酯", "二氧化钛（空气中可吸入大小的未结合颗粒物）", "五氧化二钒（正交晶型）",
								"五氯酚及其合成副产品(复合混合物)", "亚磷酸氢二甲酯", "代谢为3,3'-二甲基联苯胺的3,3'-二甲基联苯胺基染料",
								"代谢成3,3'-二甲氧基联苯胺的3,3'-二甲氧基联苯胺基染料", "全氟壬酸(PFNA)及其盐类", "全氟辛烷磺酸(PFOS)及其盐类和转化和降解前体",
								"六亚甲基亚硝胺", "出生0至28天内的新生儿男婴", "双(2-氯-1-甲基乙基)醚, 技术级", "双酚 S (BPS)",
								"反式-2-[(二甲基氨基)甲基亚氨基]-5-[2-(5-硝基-2-呋喃基)乙烯基]-1,3,4-恶二唑", "含有硫酸的强无机酸雾", "含有马兜铃属植物的草药",
								"四氢呋喃", "在溶液中易于离解的氰化盐(以氰化物计)", "多氯联苯(按分子量计含60-::-或更多氯)",
								"对氯-α，α，α-三氟甲苯（对氯三氟化苯，PCBTF）", "对硝基苯甲醚", "成人b", "无色孔雀石绿", "来源于镰刀菌（疣状镰刀菌）的毒素",
								"椰子油二乙醇胺缩合物（椰油酰胺二乙醇胺）", "氟代-浅闪石纤维状角闪石", "氧化铟锡", "氯乙烯(1,1-二氯乙烯)",
								"氯化石蜡（平均链长，C12；按重量计氯含量约为 60%）", "氰化氢", "氰化钠", "氰化钾", "炭黑（空气中可吸入大小的未结合颗粒物）",
								"煤烟、焦油和矿物油（未经处理和轻度处理的机油以及用过的发动机油）", "煤焦油沥青", "甲基丙烯酸缩水甘油酯", "甲基丙烯酸酯",
								"盐酸氮芥（盐酸二氯甲基二乙胺）", "碳化硅晶须", "结晶二氧化硅（空气中可吸入大小的颗粒物）", "蒽", "血管紧张素转换酶(ACE)抑制剂",
								"视黄醇/视黄醇酯，当每日剂量超过 10,000 IU 或 3,000 视黄醇当量时。", "贝伐单抗", "酒精饮料", "酒精饮料（酒精滥用相关）",
								"镍精炼厂灰尘来自热金属冶炼过程", "镍（可溶性化合物）", "长于5微米的帕力石棉纤维", "雌激素-孕激素（组合）用于更年期治疗", "高温未精炼菜籽油的排放",
								"龙胆紫（结晶紫）"
							]
						}
					},
					"compliance_type": {
						"title": "加州 65 号提案 警告类型",
						"description": "选择适用于您的产品的警告类型（如有）。您证明所提供的警告符合法律要求，并且只有在法律不再要求时，您才会删除之前提供的警告。",
						"editable": true,
						"hidden": false,
						"examples": ["Furniture"],
						"type": "string",
						"enum": ["alcoholic_beverage", "chemical", "diesel_engines", "food", "furniture",
							"on_product_cancer", "on_product_combined_cancer_reproductive",
							"on_product_reproductive", "passenger_or_off_road_vehicle", "raw_wood",
							"recreational_vessel"
						],
						"enumNames": ["Alcoholic Beverage", "Chemical", "Diesel Engines", "Food", "Furniture",
							"On Product Cancer", "On Product Combined Cancer Reproductive",
							"On Product Reproductive", "Passenger or Off Road Vehicle", "Raw Wood",
							"Recreational Vessel"
						]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"pesticide_marking": {
			"title": "农药标识",
			"description": "提供物品或包装上的任何农药标识。",
			"examples": ["EPA 标识编号"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 3,
			"selectors": ["marketplace_id", "marking_type"],
			"items": {
				"type": "object",
				"required": ["marking_type"],
				"properties": {
					"certification_number": {
						"title": "农药认证编号",
						"description": "提供与标识类型相对应的任何农药认证编号",
						"editable": true,
						"hidden": false,
						"examples": ["138263"],
						"type": "string",
						"maxLength": 300
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"marking_type": {
						"title": "农药标识",
						"description": "提供物品或包装上的任何农药标识。",
						"editable": true,
						"hidden": false,
						"examples": ["EPA 标识编号"],
						"type": "string",
						"enum": ["epa_establishment_number", "epa_registration_number"],
						"enumNames": ["EPA Establishment Number", "EPA Registration Number"]
					},
					"registration_status": {
						"title": "农药登记状况",
						"description": "提供商品是否需要注册的状态以及相关的注册号。",
						"editable": true,
						"hidden": false,
						"examples": ["产品不是美国《联邦杀虫剂、杀菌剂及灭鼠剂法》所定义的杀虫剂或杀虫剂设备。"],
						"type": "string",
						"enum": ["fifra_registration_exempt", "fifra_not_considered_pesticide",
							"fifra_registration_required"
						],
						"enumNames": ["根据美国《联邦杀虫剂、杀菌剂和杀鼠剂法》，该产品有资格豁免注册。", "该产品不是美国《联邦杀虫剂、杀菌剂及灭鼠剂法》所定义的杀虫剂或杀虫剂设备。",
							"该产品是美国《联邦杀虫剂、杀菌剂及灭鼠剂法》所定义的杀虫剂或杀虫剂设备"
						]
					}
				},
				"additionalProperties": false
			}
		},
		"fcc_radio_frequency_emission_compliance": {
			"title": "FCC 射频发射合规性",
			"description": "提供有关可能发射射频的产品符合 FCC 法规的详细信息。",
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["registration_status"],
				"properties": {
					"registration_status": {
						"title": "射频发射和授权状态",
						"description": "请说明该产品是否能够发射射频能量，如果能够，说明产品具有哪种类型的 FCC RF 设备授权。",
						"editable": true,
						"hidden": false,
						"examples": ["产品具有 FCC 标识"],
						"type": "string",
						"enum": ["has_fcc_id", "fcc_supplier_declaration_of_conformity",
							"not_capable_emitting_rf_energy", "fcc_incidental_radiator"
						],
						"enumNames": ["产品具有 FCC ID", "产品具有符合 FCC 规则的供应商合规性声明 (SDoC)", "产品无法发射射频能量",
							"产品是 FCC 定义的附带散热器。其设计用途并非有意使用、产生或发射超过 9 kHz 的射频能量。它不需要 FCC 射频设备授权。"
						]
					},
					"identification_number": {
						"title": "FCC ID",
						"description": "如果设备具有 FCC ID，请提供。",
						"editable": true,
						"hidden": false,
						"examples": ["2AFZZ-XMSF10G"],
						"type": "string",
						"maxLength": 100
					},
					"point_of_contact_name": {
						"title": "SDoC 联系人姓名",
						"description": "如果设备具有供应商的符合性声明，请按照 FCC 的规定为责任方提供联系点名称。",
						"editable": true,
						"hidden": false,
						"examples": ["John Doe"],
						"type": "string",
						"maxLength": 100
					},
					"point_of_contact_address": {
						"title": "SDoC 联系人美国邮寄地址",
						"description": "如果设备具有供应商的符合性声明，请按照 FCC 的规定为责任方提供美国的邮寄地址。",
						"editable": true,
						"hidden": false,
						"examples": ["橡树街 500 号\r\r\n伊利诺伊州，芝加哥 60707"],
						"type": "string",
						"maxLength": 2500
					},
					"point_of_contact_email": {
						"title": "SDoC 联系人电子邮件地址",
						"description": "如果设备具有 SDoC，请按照 FCC 的规定为责任方提供电子邮件地址（在此字段中）或美国电话号码（在下一个字段中）。如果在下一个字段中提供电话号码，则可以在此字段中输入 \"N/A\"。",
						"editable": true,
						"hidden": false,
						"examples": ["jdoe@example.eg"],
						"type": "string",
						"maxLength": 300
					},
					"point_of_contact_phone_number": {
						"title": "SDOC 联系人美国电话号码",
						"description": "如果设备具有 SDoC，请按照 FCC 的规定为责任方提供美国电话号码（在此字段中）或电子邮件地址（在上一个字段中）。如果在上一个字段中提供了电子邮件地址，则可以在此字段中输入 \"N/A\"。",
						"editable": true,
						"hidden": false,
						"examples": ["777-767-1000"],
						"type": "string",
						"maxLength": 100
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"regulatory_compliance_certification": {
			"title": "合规认证",
			"description": "提供与产品相关的任何监管以及任何所需的监管标识，例如认证编号。",
			"examples": ["FDA 510(k) 编号，\r\nF2345G234"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 5,
			"selectors": ["marketplace_id", "regulation_type"],
			"items": {
				"type": "object",
				"required": ["regulation_type", "value"],
				"properties": {
					"regulation_type": {
						"title": "合规监管类型",
						"description": "选择适用的监管类型",
						"editable": true,
						"hidden": false,
						"examples": ["CDPR 害虫鉴定"],
						"type": "string",
						"enum": ["carb_eo", "cdpr_pest_id", "certificate_of_conformity", "fda_510_k",
							"intertek_certificate_no", "tuv_certificate_no", "ul_cetrification_no", "wasda_pest_id",
							"national_organic_program_id", "energy_star_unique_id"
						],
						"enumNames": ["CARB EO", "CDPR 害虫鉴定", "EPA 符合性证书 (CoC)", "FDA 510(k) 编号", "Intertek 证书编号",
							"TUV 证书编号", "UL 证书编号", "WASDA 害虫鉴定", "国家有机计划 ID", "能源之星唯一 ID"
						]
					},
					"value": {
						"title": "监管标识",
						"description": "提供与监管类型相关的监管标识。",
						"editable": true,
						"hidden": false,
						"examples": ["1AB1331-121A"],
						"type": "string",
						"minLength": 1,
						"maxLength": 135
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"dsa_responsible_party_address": {
			"title": "负责人的电子邮件或电子地址",
			"description": "提供责任方的电子邮件地址标识符。这是存储在 RsP 注册表中的责任方联系信息。",
			"examples": ["责任方-电子邮件@example.com"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "负责人的电子邮件或电子地址",
						"description": "提供责任方的电子邮件地址标识符。这是存储在 RsP 注册表中的责任方联系信息。",
						"editable": true,
						"hidden": false,
						"examples": ["责任方-电子邮件@example.com"],
						"type": "string",
						"maxLength": 1000
					}
				},
				"additionalProperties": false
			}
		},
		"compliance_media": {
			"title": "合规媒介",
			"description": "提供有关任何合规媒体的信息，包括类型和媒体位置（例如，有效的 URL）。",
			"examples": ["安装手册"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id", "content_type", "content_language"],
			"items": {
				"type": "object",
				"required": ["content_language", "content_type", "source_location"],
				"properties": {
					"content_type": {
						"title": "合规媒介内容类型",
						"description": "请输入合规性文档的内容类型。",
						"editable": true,
						"hidden": false,
						"examples": ["安全数据表"],
						"type": "string",
						"enum": ["instructions_for_use", "compatibility_guide", "certificate_of_analysis",
							"certificate_of_compliance", "safety_information", "safety_data_sheet",
							"installation_manual", "application_guide", "patient_fact_sheet", "provider_fact_sheet",
							"troubleshooting_guide", "user_manual", "user_guide", "emergency_use_authorization",
							"emergency_use_authorization_amendment", "specification_sheet", "warranty"
						],
						"enumNames": ["使用说明", "兼容性指南", "分析认证", "合规认证", "安全信息", "安全数据表", "安装手册", "应用指南", "患者情况说明书",
							"提供者情况说明书", "故障排除指南", "用户手册", "用户指南", "紧急使用授权", "紧急使用授权修正案", "规格说明书", "质保"
						]
					},
					"content_language": {
						"title": "合规媒介语言",
						"description": "提供用于合规性媒体内容的语言。",
						"editable": true,
						"hidden": false,
						"examples": ["en_US"],
						"type": "string",
						"enum": ["ar_AE", "ar_BH", "ar_DZ", "ar_EG", "ar_IQ", "ar_JO", "ar_KW", "ar_LB", "ar_LY",
							"ar_MA", "ar_OM", "ar_QA", "ar_SA", "ar_SD", "ar_SY", "ar_TN", "ar_YE", "az_AZ",
							"be_BY", "bg_BG", "bn_IN", "bs_BA", "ca_AD", "ca_ES", "cs_CZ", "da_DK", "de_AT",
							"de_CH", "de_DE", "de_LU", "el_CY", "el_GR", "en_AE", "en_AU", "en_CA", "en_GB",
							"en_IE", "en_IN", "en_MT", "en_NG", "en_NZ", "en_PH", "en_SG", "en_US", "en_ZA",
							"es_AR", "es_BO", "es_CL", "es_CO", "es_CR", "es_DO", "es_EC", "es_ES", "es_GT",
							"es_HN", "es_MX", "es_NI", "es_PA", "es_PE", "es_PR", "es_PY", "es_SV", "es_US",
							"es_UY", "es_VE", "et_EE", "fi_FI", "fil", "fil_PH", "fr_BE", "fr_CA", "fr_CH", "fr_FR",
							"fr_LU", "ga_IE", "gu_IN", "he_IL", "hi_IN", "hr_HR", "hu_HU", "id_ID", "in_ID",
							"is_IS", "it_CH", "it_IT", "iw_IL", "ja_JP", "ka_GE", "kn_IN", "ko_KR", "lt_LT",
							"lv_LV", "mk_MK", "ml_IN", "mr_IN", "ms_MY", "mt_MT", "nb_NO", "nl_BE", "nl_NL",
							"no_NO", "pl_PL", "pt_BR", "pt_PT", "ro_RO", "ru_RU", "sk_SK", "sl_SI", "sq_AL",
							"sr_BA", "sr_CS", "sr_ME", "sr_RS", "sv_SE", "ta_IN", "te_IN", "th_TH", "tr_TR",
							"uk_UA", "vi_VN", "zh_CN", "zh_HK", "zh_SG", "zh_TW"
						],
						"enumNames": ["ar_AE", "ar_BH", "ar_DZ", "ar_EG", "ar_IQ", "ar_JO", "ar_KW", "ar_LB", "ar_LY",
							"ar_MA", "ar_OM", "ar_QA", "ar_SA", "ar_SD", "ar_SY", "ar_TN", "ar_YE", "az_AZ",
							"be_BY", "bg_BG", "bn_IN", "bs_BA", "ca_AD", "ca_ES", "cs_CZ", "da_DK", "de_AT",
							"de_CH", "de_DE", "de_LU", "el_CY", "el_GR", "en_AE", "en_AU", "en_CA", "en_GB",
							"en_IE", "en_IN", "en_MT", "en_NG", "en_NZ", "en_PH", "en_SG", "en_US", "en_ZA",
							"es_AR", "es_BO", "es_CL", "es_CO", "es_CR", "es_DO", "es_EC", "es_ES", "es_GT",
							"es_HN", "es_MX", "es_NI", "es_PA", "es_PE", "es_PR", "es_PY", "es_SV", "es_US",
							"es_UY", "es_VE", "et_EE", "fi_FI", "fil", "fil_PH", "fr_BE", "fr_CA", "fr_CH", "fr_FR",
							"fr_LU", "ga_IE", "gu_IN", "he_IL", "hi_IN", "hr_HR", "hu_HU", "id_ID", "in_ID",
							"is_IS", "it_CH", "it_IT", "iw_IL", "ja_JP", "ka_GE", "kn_IN", "ko_KR", "lt_LT",
							"lv_LV", "mk_MK", "ml_IN", "mr_IN", "ms_MY", "mt_MT", "nb_NO", "nl_BE", "nl_NL",
							"no_NO", "pl_PL", "pt_BR", "pt_PT", "ro_RO", "ru_RU", "sk_SK", "sl_SI", "sq_AL",
							"sr_BA", "sr_CS", "sr_ME", "sr_RS", "sv_SE", "ta_IN", "te_IN", "th_TH", "tr_TR",
							"uk_UA", "vi_VN", "zh_CN", "zh_HK", "zh_SG", "zh_TW"
						]
					},
					"source_location": {
						"title": "合规媒体位置来源",
						"description": "说明合规媒体的来源位置。",
						"editable": true,
						"hidden": false,
						"examples": ["http://example.eg/sds_1.pdf"],
						"type": "string"
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"gpsr_safety_attestation": {
			"title": "GPSR 安全认证",
			"description": "如果您的产品没有任何警告和安全信息，请勾选“是”，因为即使没有此类信息，也可以安全地按预期使用。",
			"examples": ["是"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "GPSR 安全认证",
						"description": "如果您的产品没有任何警告和安全信息，请勾选“是”，因为即使没有此类信息，也可以安全地按预期使用。",
						"editable": true,
						"hidden": false,
						"examples": ["是"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					}
				},
				"additionalProperties": false
			}
		},
		"gpsr_manufacturer_reference": {
			"title": "GPSR 制造商参考",
			"description": "提供您注册表中制造商地址的参考电子邮件ID。",
			"examples": ["Abc@example.com"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": [],
				"properties": {
					"gpsr_manufacturer_email_address": {
						"title": "GPSR 制造商电子邮件地址",
						"description": "提供您注册表中制造商地址的参考电子邮件ID。",
						"editable": true,
						"hidden": false,
						"examples": ["Abc@example.com"],
						"type": "string",
						"maxLength": 100
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"ships_globally": {
			"title": "全球发货",
			"description": "提供该商品是否可以由亚马逊全球发货。",
			"examples": ["是"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "全球发货",
						"description": "提供该商品是否可以由亚马逊全球发货。",
						"editable": true,
						"hidden": false,
						"examples": ["是"],
						"type": "boolean",
						"enum": [true, false],
						"enumNames": ["是", "没有"]
					}
				},
				"additionalProperties": false
			}
		},
		"main_product_image_locator": {
			"title": "主产品图片定位器",
			"description": "提供图片的位置和来源",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "主图像链接地址",
						"description": "产品的交付主图所在的链接地址。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.main.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_product_image_locator_1": {
			"title": "其他产品图片定位器",
			"description": "提供图片的位置和来源",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片链接地址",
						"description": "产品其他图片的链接地址 。当客户点击查看与产品相关的其他视图时，这些图像将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_product_image_locator_2": {
			"title": "其他产品图片定位器",
			"description": "提供图片的位置和来源",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的链接地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_product_image_locator_3": {
			"title": "其他产品图片定位器",
			"description": "提供图片的位置和来源",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的链接地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_product_image_locator_4": {
			"title": "其他产品图片定位器",
			"description": "提供图片的位置和来源",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的链接地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_product_image_locator_5": {
			"title": "其他产品图片定位器",
			"description": "提供图片的位置和来源",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的链接地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_product_image_locator_6": {
			"title": "其他产品图片定位器 6",
			"description": "该属性表示产品的其他产品图片定位器 6",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的链接地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_product_image_locator_7": {
			"title": "其他产品图片定位器 7",
			"description": "该属性表示产品的其他产品图片定位器 7",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的链接地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"other_product_image_locator_8": {
			"title": "其他产品图片定位器 8",
			"description": "该属性表示产品的其他产品图片定位器 8",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "其他图片的链接地址",
						"description": "其他产品图片的链接地址。当客户点击查看与产品相关的其他视图时，这些图片将显示在详细信息页面上。",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.other1.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"swatch_product_image_locator": {
			"title": "样本产品图片定位器",
			"description": "提供图片的位置和来源",
			"examples": ["供稿"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["media_location"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"media_location": {
						"title": "样品图片的链接地址",
						"description": "产品的彩色样品图片所在的链接地址",
						"editable": true,
						"hidden": false,
						"examples": ["http://www.companyname.com/images/1250.swatch.jpg"],
						"type": "string",
						"format": "uri",
						"pattern": "^(https?|s3)://"
					}
				},
				"additionalProperties": false
			}
		},
		"item_dimensions": {
			"title": "产品尺寸",
			"description": "提供商品尺寸",
			"examples": ["10 inches"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["height", "length", "width"],
				"properties": {
					"height": {
						"title": "商品高度",
						"description": "给出商品高度",
						"examples": ["2.7 英寸"],
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "商品高度单位",
								"description": "选择 Item Height 的度量单位。如果给出 Item Height 的值，还必须输入相应的单位。",
								"editable": true,
								"hidden": false,
								"examples": ["英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "商品高度",
								"description": "给出商品高度的数值。",
								"editable": true,
								"hidden": false,
								"examples": ["2.7"],
								"type": "number",
								"minimum": 0,
								"maximum": 1000000000
							}
						},
						"additionalProperties": false
					},
					"length": {
						"title": "商品长度",
						"description": "给出商品长度",
						"examples": ["10"],
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "商品长度单位",
								"description": "选择 Item Length 的度量单位。如果给出 Item Length 的值，还必须输入相应的单位。",
								"editable": true,
								"hidden": false,
								"examples": ["英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "商品长度",
								"description": "给出商品长度的数值。",
								"editable": true,
								"hidden": false,
								"examples": ["10"],
								"type": "number",
								"minimum": 0,
								"maximum": 1000000000
							}
						},
						"additionalProperties": false
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"width": {
						"title": "商品宽度",
						"description": "给出商品宽度",
						"examples": ["2"],
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "商品宽度单位",
								"description": "选择 Item Width 的度量单位。如果给出 Item Width 的值，还必须输入相应的单位。",
								"editable": true,
								"hidden": false,
								"examples": ["英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "商品宽度",
								"description": "给出商品宽度的数值。",
								"editable": true,
								"hidden": false,
								"examples": ["2"],
								"type": "number",
								"minimum": 0,
								"maximum": 1000000000
							}
						},
						"additionalProperties": false
					}
				},
				"additionalProperties": false
			}
		},
		"item_package_dimensions": {
			"title": "包装尺寸",
			"description": "package_width",
			"examples": ["50 x 75 x 60厘米"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["height", "length", "width"],
				"properties": {
					"height": {
						"title": "包装高度",
						"description": "给出包装高度",
						"examples": ["50厘米"],
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "包装高度单位",
								"description": "选择 Package Height 的度量单位。如果给出 Package Height 的值，还必须输入相应的单位。",
								"editable": true,
								"hidden": false,
								"examples": ["毫米，厘米，英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "包装高度",
								"description": "给出包装高度的数值。",
								"editable": true,
								"hidden": false,
								"examples": ["50，75，60"],
								"type": "number",
								"minimum": 0,
								"maximum": 1000000000
							}
						},
						"additionalProperties": false
					},
					"length": {
						"title": "包装长度",
						"description": "给出包装长度",
						"examples": ["75厘米"],
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "包装长度单位",
								"description": "选择 Package Length 的度量单位。如果给出 Package Length 的值，还必须输入相应的单位。",
								"editable": true,
								"hidden": false,
								"examples": ["毫米，厘米，英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "包装长度",
								"description": "给出包装长度的数值。",
								"editable": true,
								"hidden": false,
								"examples": ["50，75，60"],
								"type": "number",
								"minimum": 0,
								"maximum": 1000000000
							}
						},
						"additionalProperties": false
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"width": {
						"title": "包装宽度",
						"description": "给出包装宽度",
						"examples": ["60厘米"],
						"type": "object",
						"required": ["unit", "value"],
						"properties": {
							"unit": {
								"title": "包装宽度单位",
								"description": "选择 Package Width 的度量单位。如果给出 Package Width 的值，还必须输入相应的单位。",
								"editable": true,
								"hidden": false,
								"examples": ["毫米，厘米，英寸"],
								"type": "string",
								"enum": ["angstrom", "mils", "decimeters", "kilometers", "centimeters", "micrometer",
									"millimeters", "hundredths_inches", "picometer", "yards", "meters", "nanometer",
									"inches", "feet", "miles"
								],
								"enumNames": ["Angstrom", "Mils", "分米", "千米", "厘米", "微米", "毫米", "百分之一英寸", "皮秒计", "码",
									"米", "纳米", "英寸", "英尺", "英里"
								]
							},
							"value": {
								"title": "包装宽度",
								"description": "给出包装宽度的数值。",
								"editable": true,
								"hidden": false,
								"examples": ["50，75，60"],
								"type": "number",
								"minimum": 0,
								"maximum": 1000000000
							}
						},
						"additionalProperties": false
					}
				},
				"additionalProperties": false
			}
		},
		"item_package_weight": {
			"title": "包裹重量",
			"description": "可交运包裹的重量。这个值用于“卓越亚马逊物流”。",
			"examples": ["0.65 磅"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["unit", "value"],
				"properties": {
					"value": {
						"title": "包装重量",
						"description": "给出包装重量的数值。",
						"editable": true,
						"hidden": false,
						"examples": ["0.65"],
						"type": "number",
						"exclusiveMinimum": 0,
						"minimum": 0,
						"maximum": 1000000000
					},
					"unit": {
						"title": "包装重量单位",
						"description": "选择 Package Weight 的度量单位。如果给出 Package Weight 的值，还必须输入相应的单位。",
						"editable": true,
						"hidden": false,
						"examples": ["磅"],
						"type": "string",
						"enum": ["grams", "kilograms", "tons", "milligrams", "hundredths_pounds", "ounces", "pounds"],
						"enumNames": ["克", "千克", "吨", "毫克", "百分之一磅", "盎司", "磅"]
					},
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					}
				},
				"additionalProperties": false
			}
		},
		"master_pack_layers_per_pallet_quantity": {
			"title": "每托盘主包装层数",
			"description": "提供存放在托盘上的主包装层数（即 Hi）。",
			"examples": ["5"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "每托盘主包装层数",
						"description": "提供存放在托盘上的主包装层数（即 Hi）。",
						"editable": true,
						"hidden": false,
						"examples": ["5"],
						"type": "integer",
						"minimum": 1,
						"maximum": 10000
					}
				},
				"additionalProperties": false
			}
		},
		"master_packs_per_layer_quantity": {
			"title": "每层主包装数量",
			"description": "提供托盘每层中的产品主包装数量（即 Ti）。",
			"examples": ["9"],
			"type": "array",
			"minItems": 1,
			"minUniqueItems": 1,
			"maxUniqueItems": 1,
			"selectors": ["marketplace_id"],
			"items": {
				"type": "object",
				"required": ["value"],
				"properties": {
					"marketplace_id": {
						"$ref": "#/$defs/marketplace_id"
					},
					"value": {
						"title": "每层主包装数量",
						"description": "提供托盘每层中的产品主包装数量（即 Ti）。",
						"editable": true,
						"hidden": false,
						"examples": ["9"],
						"type": "integer",
						"minimum": 1,
						"maximum": 10000
					}
				},
				"additionalProperties": false
			}
		}
	},
	"additionalProperties": false,
	"allOf": [{
		"if": {
			"anyOf": [{
				"allOf": [{
					"not": {
						"required": ["merchant_suggested_asin"],
						"properties": {
							"merchant_suggested_asin": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"required": ["supplier_declared_has_product_identifier_exemption"],
					"properties": {
						"supplier_declared_has_product_identifier_exemption": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": [false]
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["merchant_suggested_asin"],
						"properties": {
							"merchant_suggested_asin": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"required": ["supplier_declared_has_product_identifier_exemption"],
					"properties": {
						"supplier_declared_has_product_identifier_exemption": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": [false]
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["merchant_suggested_asin"],
						"properties": {
							"merchant_suggested_asin": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["supplier_declared_has_product_identifier_exemption"],
						"properties": {
							"supplier_declared_has_product_identifier_exemption": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["merchant_suggested_asin"],
						"properties": {
							"merchant_suggested_asin": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["supplier_declared_has_product_identifier_exemption"],
						"properties": {
							"supplier_declared_has_product_identifier_exemption": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["externally_assigned_product_identifier"]
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"not": {
						"required": ["externally_assigned_product_identifier"],
						"properties": {
							"externally_assigned_product_identifier": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"required": ["supplier_declared_has_product_identifier_exemption"],
					"properties": {
						"supplier_declared_has_product_identifier_exemption": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": [false]
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["externally_assigned_product_identifier"],
						"properties": {
							"externally_assigned_product_identifier": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"required": ["supplier_declared_has_product_identifier_exemption"],
					"properties": {
						"supplier_declared_has_product_identifier_exemption": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": [false]
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["externally_assigned_product_identifier"],
						"properties": {
							"externally_assigned_product_identifier": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["supplier_declared_has_product_identifier_exemption"],
						"properties": {
							"supplier_declared_has_product_identifier_exemption": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["externally_assigned_product_identifier"],
						"properties": {
							"externally_assigned_product_identifier": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["supplier_declared_has_product_identifier_exemption"],
						"properties": {
							"supplier_declared_has_product_identifier_exemption": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["merchant_suggested_asin"]
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["package_level"],
					"properties": {
						"package_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["case", "pallet"]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["package_level"],
					"properties": {
						"package_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["case", "pallet"]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["package_contains_sku"]
		}
	}, {
		"if": {
			"anyOf": [{
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"items": {
								"required": ["value"]
							}
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["age_range_description", "generic_keyword", "lifestyle", "manufacturer",
				"target_audience_keyword", "target_gender"
			]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["AGE_GENDER_CATEGORY"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["age_gender_category"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["MODEL_NUMBER/SIZE", "MODEL", "MODEL/SIZE_NAME"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["model_number"]
		}
	}, {
		"properties": {
			"fulfillment_availability": {
				"items": {
					"if": {
						"allOf": [{
							"required": ["fulfillment_channel_code"],
							"properties": {
								"fulfillment_channel_code": {
									"enum": ["DEFAULT"]
								}
							}
						}, {
							"not": {
								"required": ["is_inventory_available"]
							}
						}]
					},
					"then": {
						"required": ["quantity"]
					}
				}
			}
		}
	}, {
		"properties": {
			"fulfillment_availability": {
				"items": {
					"if": {
						"not": {
							"allOf": [{
								"required": ["fulfillment_channel_code"],
								"properties": {
									"fulfillment_channel_code": {
										"enum": ["DEFAULT"]
									}
								}
							}, {
								"not": {
									"required": ["is_inventory_available"]
								}
							}]
						}
					},
					"then": {
						"not": {
							"required": ["quantity"]
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"fulfillment_availability": {
				"items": {
					"if": {
						"not": {
							"required": ["fulfillment_channel_code"],
							"properties": {
								"fulfillment_channel_code": {
									"enum": ["DEFAULT"]
								}
							}
						}
					},
					"then": {
						"not": {
							"required": ["lead_time_to_ship_max_days"]
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"fulfillment_availability": {
				"items": {
					"if": {
						"not": {
							"required": ["fulfillment_channel_code"],
							"properties": {
								"fulfillment_channel_code": {
									"enum": ["DEFAULT"]
								}
							}
						}
					},
					"then": {
						"not": {
							"required": ["restock_date"]
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"fulfillment_availability": {
				"items": {
					"if": {
						"allOf": [{
							"required": ["fulfillment_channel_code"],
							"properties": {
								"fulfillment_channel_code": {
									"enum": ["DEFAULT"]
								}
							}
						}, {
							"not": {
								"required": ["quantity"]
							}
						}]
					},
					"then": {
						"required": ["is_inventory_available"]
					}
				}
			}
		}
	}, {
		"properties": {
			"fulfillment_availability": {
				"items": {
					"if": {
						"not": {
							"allOf": [{
								"required": ["fulfillment_channel_code"],
								"properties": {
									"fulfillment_channel_code": {
										"enum": ["DEFAULT"]
									}
								}
							}, {
								"not": {
									"required": ["quantity"]
								}
							}]
						}
					},
					"then": {
						"not": {
							"required": ["is_inventory_available"]
						}
					}
				}
			}
		}
	}, {
		"if": {
			"allOf": [{
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"not": {
					"required": ["skip_offer"],
					"properties": {
						"skip_offer": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": [true]
									}
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["fulfillment_availability"]
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"properties": {
						"map_price": {
							"properties": {
								"schedule": {
									"if": {
										"required": ["currency"],
										"properties": {
											"currency": {
												"enum": ["JPY"]
											}
										}
									},
									"then": {
										"properties": {
											"value_with_tax": {
												"multipleOf": 1
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"if": {
						"anyOf": [{
							"required": ["audience"],
							"properties": {
								"audience": {
									"enum": ["ALL"]
								}
							}
						}, {
							"not": {
								"required": ["audience"]
							}
						}]
					},
					"then": {
						"properties": {
							"map_price": {
								"maxItems": 1
							}
						}
					},
					"else": {
						"not": {
							"required": ["map_price"]
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"properties": {
						"our_price": {
							"properties": {
								"schedule": {
									"if": {
										"required": ["currency"],
										"properties": {
											"currency": {
												"enum": ["JPY"]
											}
										}
									},
									"then": {
										"properties": {
											"value_with_tax": {
												"multipleOf": 1
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"if": {
						"anyOf": [{
							"required": ["discounted_price"],
							"properties": {
								"discounted_price": {
									"items": {
										"required": ["schedule"],
										"properties": {
											"schedule": {
												"items": {
													"required": ["value_with_tax"]
												}
											}
										}
									}
								}
							}
						}, {
							"required": ["audience"],
							"properties": {
								"audience": {
									"enum": ["B2B"]
								}
							}
						}]
					},
					"then": {
						"required": ["our_price"],
						"properties": {
							"our_price": {
								"minItems": 1
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"properties": {
						"minimum_seller_allowed_price": {
							"properties": {
								"schedule": {
									"if": {
										"required": ["currency"],
										"properties": {
											"currency": {
												"enum": ["JPY"]
											}
										}
									},
									"then": {
										"properties": {
											"value_with_tax": {
												"multipleOf": 1
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"if": {
						"anyOf": [{
							"required": ["audience"],
							"properties": {
								"audience": {
									"enum": ["ALL"]
								}
							}
						}, {
							"required": ["audience"],
							"properties": {
								"audience": {
									"enum": ["B2B"]
								}
							}
						}, {
							"not": {
								"required": ["audience"]
							}
						}]
					},
					"then": {
						"properties": {
							"minimum_seller_allowed_price": {
								"maxItems": 1
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"properties": {
						"maximum_seller_allowed_price": {
							"properties": {
								"schedule": {
									"if": {
										"required": ["currency"],
										"properties": {
											"currency": {
												"enum": ["JPY"]
											}
										}
									},
									"then": {
										"properties": {
											"value_with_tax": {
												"multipleOf": 1
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"if": {
						"anyOf": [{
							"required": ["audience"],
							"properties": {
								"audience": {
									"enum": ["ALL"]
								}
							}
						}, {
							"required": ["audience"],
							"properties": {
								"audience": {
									"enum": ["B2B"]
								}
							}
						}, {
							"not": {
								"required": ["audience"]
							}
						}]
					},
					"then": {
						"properties": {
							"maximum_seller_allowed_price": {
								"maxItems": 1
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"properties": {
						"discounted_price": {
							"properties": {
								"schedule": {
									"if": {
										"required": ["currency"],
										"properties": {
											"currency": {
												"enum": ["JPY"]
											}
										}
									},
									"then": {
										"properties": {
											"value_with_tax": {
												"multipleOf": 1
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"if": {
						"anyOf": [{
							"required": ["audience"],
							"properties": {
								"audience": {
									"enum": ["ALL"]
								}
							}
						}, {
							"not": {
								"required": ["audience"]
							}
						}]
					},
					"then": {
						"properties": {
							"discounted_price": {
								"maxItems": 1
							}
						}
					},
					"else": {
						"not": {
							"required": ["discounted_price"]
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"properties": {
						"quantity_discount_plan": {
							"properties": {
								"schedule": {
									"properties": {
										"levels": {
											"if": {
												"required": ["currency"],
												"properties": {
													"currency": {
														"enum": ["JPY"]
													}
												}
											},
											"then": {
												"properties": {
													"value": {
														"multipleOf": 1
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["purchasable_offer"],
			"properties": {
				"purchasable_offer": {
					"contains": {
						"required": ["quantity_discount_plan"],
						"properties": {
							"quantity_discount_plan": {
								"contains": {
									"required": ["schedule"],
									"properties": {
										"schedule": {
											"contains": {
												"required": ["discount_type"],
												"properties": {
													"discount_type": {
														"enum": ["percent"]
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"purchasable_offer": {
					"items": {
						"properties": {
							"quantity_discount_plan": {
								"properties": {
									"schedule": {
										"properties": {
											"levels": {
												"properties": {
													"value": {
														"maximum": 99
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"purchasable_offer": {
				"items": {
					"if": {
						"required": ["audience"],
						"properties": {
							"audience": {
								"enum": ["B2B"]
							}
						}
					},
					"then": {
						"properties": {
							"quantity_discount_plan": {
								"maxItems": 1
							}
						}
					},
					"else": {
						"not": {
							"required": ["quantity_discount_plan"]
						}
					}
				}
			}
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["skip_offer"],
						"properties": {
							"skip_offer": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": [true]
										}
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["skip_offer"],
						"properties": {
							"skip_offer": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": [true]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["condition_type"]
		}
	}, {
		"properties": {
			"list_price": {
				"items": {
					"if": {
						"required": ["currency"]
					},
					"then": {
						"required": ["value"]
					}
				}
			}
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["skip_offer"],
						"properties": {
							"skip_offer": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": [true]
										}
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["skip_offer"],
						"properties": {
							"skip_offer": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": [true]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["list_price"]
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["fulfillment_availability"],
					"properties": {
						"fulfillment_availability": {
							"contains": {
								"required": ["fulfillment_channel_code"],
								"properties": {
									"fulfillment_channel_code": {
										"enum": ["DEFAULT"]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["skip_offer"],
						"properties": {
							"skip_offer": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": [true]
										}
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["fulfillment_availability"],
					"properties": {
						"fulfillment_availability": {
							"contains": {
								"required": ["fulfillment_channel_code"],
								"properties": {
									"fulfillment_channel_code": {
										"enum": ["DEFAULT"]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["skip_offer"],
						"properties": {
							"skip_offer": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": [true]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["merchant_shipping_group"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["COLOR_NAME/STYLE_NAME", "STYLE_NAME/COLOR_NAME",
										"STYLE_NAME", "NUMBER_OF_ITEMS/STYLE_NAME",
										"COLOR_NAME/SIZE_NAME/STYLE_NAME",
										"SIZE_NAME/STYLE_NAME/COLOR_NAME", "STYLE_NAME/SIZE_NAME",
										"SIZE_NAME/STYLE_NAME"
									]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["style"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["MATERIAL_TYPE/ITEM_FORM/SIZE_NAME",
										"MATERIAL_TYPE/COLOR_NAME", "MATERIAL_TYPE",
										"MATERIAL_TYPE/SIZE_NAME", "COLOR_NAME/MATERIAL_TYPE",
										"SIZE_NAME/MATERIAL_TYPE"
									]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["material"]
		}
	}, {
		"allOf": [{
			"if": {
				"anyOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			},
			"then": {
				"required": ["number_of_items"]
			}
		}, {
			"if": {
				"allOf": [{
					"required": ["child_parent_sku_relationship"],
					"properties": {
						"child_parent_sku_relationship": {
							"items": {
								"required": ["parent_sku"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"required": ["variation_theme"],
					"properties": {
						"variation_theme": {
							"contains": {
								"required": ["name"],
								"properties": {
									"name": {
										"enum": ["SIZE/COLOR/NUMBER_OF_ITEMS",
											"COLOR/NUMBER_OF_ITEMS",
											"SIZE_NAME/NUMBER_OF_ITEMS",
											"COLOR_NAME/NUMBER_OF_ITEMS",
											"SIZE/NUMBER_OF_ITEMS", "NUMBER_OF_ITEMS",
											"SIZE_NAME/COLOR_NAME/NUMBER_OF_ITEMS",
											"NUMBER_OF_ITEMS/STYLE_NAME"
										]
									}
								}
							}
						}
					}
				}]
			},
			"then": {
				"required": ["number_of_items"]
			}
		}]
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["ITEM_PACKAGE_QUANTITY/COLOR_NAME",
										"ITEM_PACKAGE_QUANTITY", "COLOR_NAME/ITEM_PACKAGE_QUANTITY"
									]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["item_package_quantity"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["SPECIAL_SIZE_TYPE/SIZE_NAME/COLOR_NAME"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["special_size_type"]
		}
	}, {
		"properties": {
			"color": {
				"items": {
					"if": {
						"not": {
							"required": ["value"]
						}
					},
					"then": {
						"required": ["standardized_values"],
						"properties": {
							"standardized_values": {
								"minItems": 1
							}
						}
					}
				}
			}
		}
	}, {
		"allOf": [{
			"if": {
				"anyOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			},
			"then": {
				"required": ["color"]
			}
		}, {
			"if": {
				"allOf": [{
					"required": ["child_parent_sku_relationship"],
					"properties": {
						"child_parent_sku_relationship": {
							"items": {
								"required": ["parent_sku"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"required": ["variation_theme"],
					"properties": {
						"variation_theme": {
							"contains": {
								"required": ["name"],
								"properties": {
									"name": {
										"enum": ["SIZE/COLOR/NUMBER_OF_ITEMS",
											"TEAM_NAME/SIZE_NAME/COLOR_NAME",
											"PATTERN_NAME/COLOR_NAME",
											"ITEM_PACKAGE_QUANTITY/COLOR_NAME",
											"COLOR/NUMBER_OF_ITEMS",
											"COLOR_NAME/STYLE_NAME",
											"COLOR_NAME/METAL_TYPE", "SIZE/COLOR",
											"COLOR_NAME/NUMBER_OF_ITEMS",
											"MATERIAL_TYPE/COLOR_NAME",
											"COLOR_NAME/ITEM_PACKAGE_QUANTITY",
											"STYLE_NAME/COLOR_NAME", "COLOR_NAME",
											"COLOR_NAME/FLAVOR_NAME",
											"SIZE_NAME/COLOR_NAME/NUMBER_OF_ITEMS", "COLOR",
											"SIZE_NAME/COLOR_NAME/PATTERN_NAME",
											"COLOR_NAME/SIZE_NAME",
											"COLOR_NAME/MATERIAL_TYPE", "COLOR/SIZE",
											"COLOR_NAME/RING_SIZE",
											"COLOR_NAME/SIZE_NAME/STYLE_NAME",
											"SPECIAL_SIZE_TYPE/SIZE_NAME/COLOR_NAME",
											"COLOR_NAME/ITEM_DISPLAY_LENGTH",
											"SIZE_NAME/STYLE_NAME/COLOR_NAME",
											"SIZE_NAME/COLOR_NAME",
											"COLOR_NAME/PATTERN_NAME"
										]
									}
								}
							}
						}
					}
				}]
			},
			"then": {
				"required": ["color"]
			}
		}]
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["MODEL_NUMBER/SIZE", "SIZE/COLOR/NUMBER_OF_ITEMS",
										"TEAM_NAME/SIZE_NAME", "TEAM_NAME/SIZE_NAME/COLOR_NAME",
										"ITEM_FORM/SIZE_NAME", "MATERIAL_TYPE/ITEM_FORM/SIZE_NAME",
										"SIZE_NAME/NUMBER_OF_ITEMS", "SIZE", "SIZE/COLOR",
										"SIZE_NAME", "SIZE_NAME/SCENT_NAME", "SIZE/UNIT_COUNT",
										"MODEL/SIZE_NAME", "MATERIAL_TYPE/SIZE_NAME",
										"SIZE/NUMBER_OF_ITEMS",
										"SIZE_NAME/COLOR_NAME/NUMBER_OF_ITEMS",
										"SIZE_NAME/COLOR_NAME/PATTERN_NAME", "COLOR_NAME/SIZE_NAME",
										"COLOR/SIZE", "COLOR_NAME/SIZE_NAME/STYLE_NAME",
										"SPECIAL_SIZE_TYPE/SIZE_NAME/COLOR_NAME",
										"SIZE_NAME/STYLE_NAME/COLOR_NAME", "SIZE_NAME/UNIT_COUNT",
										"STYLE_NAME/SIZE_NAME", "SIZE_NAME/COLOR_NAME",
										"SIZE_NAME/MATERIAL_TYPE", "SIZE_NAME/STYLE_NAME"
									]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["size"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["COLOR_NAME/METAL_TYPE", "METAL_TYPE"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["metal_type"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["ITEM_SHAPE"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["item_shape"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["RING_SIZE", "COLOR_NAME/RING_SIZE"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["ring"],
			"properties": {
				"ring": {
					"items": {
						"required": ["size"]
					}
				}
			}
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["EDITION"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["edition"]
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["lifestyle"],
					"properties": {
						"lifestyle": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["Premium Beauty", "جمال ممتاز",
											"Premium-Schönheit", "Belleza Premium",
											"Beauté Premium", "Bellezza Premium/Luxury",
											"ラグジュアリービューティ", "Premium schoonheid",
											"Piękno Premium", "beleza premium",
											"förstklassig skönhet",
											"birinci sınıf güzellik", "高级美容"
										]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["lifestyle"],
					"properties": {
						"lifestyle": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["Premium Beauty", "جمال ممتاز",
											"Premium-Schönheit", "Belleza Premium",
											"Beauté Premium", "Bellezza Premium/Luxury",
											"ラグジュアリービューティ", "Premium schoonheid",
											"Piękno Premium", "beleza premium",
											"förstklassig skönhet",
											"birinci sınıf güzellik", "高级美容"
										]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["product_benefit", "short_product_description", "specific_uses_for_product"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["LENS_COLOR"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["lens"],
			"properties": {
				"lens": {
					"items": {
						"required": ["color"]
					}
				}
			}
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["ITEM_DISPLAY_DIAMETER", "ITEM_DISPLAY_LENGTH",
										"COLOR_NAME/ITEM_DISPLAY_LENGTH"
									]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["item_display_dimensions"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["FLAVOR_NAME", "COLOR_NAME/FLAVOR_NAME"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["flavor"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["VOLTAGE"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["voltage"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["VOLUME_CAPACITY_NAME"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["volume_capacity_name"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["PATTERN_NAME/COLOR_NAME", "PATTERN_NAME",
										"SIZE_NAME/COLOR_NAME/PATTERN_NAME", "PATTERN",
										"COLOR_NAME/PATTERN_NAME"
									]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["pattern"]
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["fulfillment_availability"],
					"properties": {
						"fulfillment_availability": {
							"contains": {
								"required": ["fulfillment_channel_code"],
								"properties": {
									"fulfillment_channel_code": {
										"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["fulfillment_availability"],
					"properties": {
						"fulfillment_availability": {
							"contains": {
								"required": ["fulfillment_channel_code"],
								"properties": {
									"fulfillment_channel_code": {
										"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"items": {
									"required": ["fulfillment_channel_code"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"not": {
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"items": {
									"required": ["fulfillment_channel_code"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["is_expiration_dated_product"]
		}
	}, {
		"if": {
			"required": ["is_expiration_dated_product"],
			"properties": {
				"is_expiration_dated_product": {
					"contains": {
						"required": ["value"],
						"properties": {
							"value": {
								"enum": [true]
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"product_expiration_type": {
					"items": {
						"required": ["value"]
					}
				},
				"fc_shelf_life": {
					"items": {
						"required": ["unit", "value"]
					}
				}
			}
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["is_expiration_dated_product"],
					"properties": {
						"is_expiration_dated_product": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": [true]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["is_expiration_dated_product"],
					"properties": {
						"is_expiration_dated_product": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": [true]
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["fc_shelf_life", "product_expiration_type"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["ITEM_FORM/SIZE_NAME", "MATERIAL_TYPE/ITEM_FORM/SIZE_NAME",
										"ITEM_FORM"
									]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["item_form"]
		}
	}, {
		"allOf": [{
			"if": {
				"anyOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			},
			"then": {
				"required": ["unit_count"]
			}
		}, {
			"if": {
				"allOf": [{
					"required": ["child_parent_sku_relationship"],
					"properties": {
						"child_parent_sku_relationship": {
							"items": {
								"required": ["parent_sku"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}, {
					"required": ["variation_theme"],
					"properties": {
						"variation_theme": {
							"contains": {
								"required": ["name"],
								"properties": {
									"name": {
										"enum": ["SIZE/UNIT_COUNT", "SIZE_NAME/UNIT_COUNT"]
									}
								}
							}
						}
					}
				}]
			},
			"then": {
				"required": ["unit_count"]
			}
		}, {
			"if": {
				"anyOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			},
			"then": {
				"required": ["unit_count"]
			}
		}, {
			"if": {
				"anyOf": [{
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			},
			"then": {
				"required": ["unit_count"]
			}
		}, {
			"if": {
				"anyOf": [{
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
										}
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"items": {
										"required": ["value"]
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
										}
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["parent"]
											}
										}
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"not": {
							"required": ["fulfillment_availability"],
							"properties": {
								"fulfillment_availability": {
									"items": {
										"required": ["fulfillment_channel_code"]
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"items": {
										"required": ["value"]
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"not": {
							"required": ["fulfillment_availability"],
							"properties": {
								"fulfillment_availability": {
									"items": {
										"required": ["fulfillment_channel_code"]
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["parent"]
											}
										}
									}
								}
							}
						}
					}]
				}]
			},
			"then": {
				"required": ["unit_count"]
			}
		}]
	}, {
		"if": {
			"required": ["league_name"],
			"properties": {
				"league_name": {
					"contains": {
						"required": ["value"],
						"properties": {
							"value": {
								"enum": ["NASCAR", "nascar", "ناسكار"]
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"team_name": {
					"items": {
						"properties": {
							"value": {
								"enum": ["A. J. Allmendinger", "Alex Bowman", "Aric Almirola", "Austin Dillon",
									"Bobby Labonte", "Brad Keselowski", "Carl Edwards", "Casey Mears",
									"Chase Elliott", "Chris Buescher", "Clint Bowyer", "Dale Earnhardt",
									"Dale Earnhardt Jr.", "Daniel Suárez", "Darrell Wallace Jr.",
									"David Ragan", "Denny Hamlin", "Elliott Sadler", "Erik Jones",
									"Gray Gaulding", "Greg Biffle", "Jamie McMurray", "Jeff Burton",
									"Jeff Gordon", "Jimmie Johnson", "Joey Logano", "Juan Pablo Montoya",
									"Kasey Kahne", "Kevin Harvick", "Kurt Busch", "Kyle Busch",
									"Kyle Larson", "Mark Martin", "Martin Truex Jr.", "Matt DiBenedetto",
									"Matt Kenseth", "Michael McDowell", "Paul Menard", "Richard Petty",
									"Ricky Stenhouse Jr.", "Ryan Blaney", "Ryan Newman", "Tony Stewart",
									"Trevor Bayne", "Ty Dillon", "William Byron"
								]
							}
						}
					}
				}
			}
		},
		"else": {
			"if": {
				"required": ["league_name"],
				"properties": {
					"league_name": {
						"contains": {
							"required": ["value"],
							"properties": {
								"value": {
									"enum": ["eスポーツ", "E-Sports (sport elettronici)", "E-sport", "E-Sports",
										"ألعاب إلكترونية", "E-Sport", "e_sports", "Deportes electrónicos"
									]
								}
							}
						}
					}
				}
			},
			"then": {
				"properties": {
					"team_name": {
						"items": {
							"properties": {
								"value": {
									"enum": ["电子竞技"]
								}
							}
						}
					}
				}
			},
			"else": {
				"if": {
					"required": ["league_name"],
					"properties": {
						"league_name": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["Pro Wrestling League", "Liga de lucha libre profesional",
											"インド/プロレスリーグ", "Liga Profissional de Luta Livre",
											"pro_wrestling_league", "Pro Wrestling Ligan",
											"دوري المصارعة للمحترفين"
										]
									}
								}
							}
						}
					}
				},
				"then": {
					"properties": {
						"team_name": {
							"items": {
								"properties": {
									"value": {
										"enum": ["NCR 旁遮普皇家队", "UP 丹加勒队", "哈里亚纳邦铁锤队", "孟买马哈拉蒂队", "尤达队", "德里苏丹队"]
									}
								}
							}
						}
					}
				},
				"else": {
					"if": {
						"required": ["league_name"],
						"properties": {
							"league_name": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["Formel 1", "Formuła 1", "formula_1", "Fórmula 1",
												"Formula 1", "فورمولا 1", "Formule 1", "フォーミュラ1"
											]
										}
									}
								}
							}
						}
					},
					"then": {
						"properties": {
							"team_name": {
								"items": {
									"properties": {
										"value": {
											"enum": ["Aston Martin", "BWT 赛点车队", "Mclaren", "Red Bull Racing",
												"哈斯 F1 车队", "威廉姆斯车队", "梅赛德斯 AMG 马石油车队", "法拉利车队", "艾法托利车队",
												"阿尔法罗密欧车队", "雷诺 DP World 车队", "高山"
											]
										}
									}
								}
							}
						}
					},
					"else": {
						"if": {
							"required": ["league_name"],
							"properties": {
								"league_name": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["I-ligan", "I-League", "インド/Iリーグ", "Liga I",
													"i_league", "الدوري الهندي للمحترفين"
												]
											}
										}
									}
								}
							}
						},
						"then": {
							"properties": {
								"team_name": {
									"items": {
										"properties": {
											"value": {
												"enum": ["NEROCA FC", "丘吉尔兄弟足球俱乐部", "印第安阿罗足球俱乐部",
													"古库拉姆喀拉拉邦足球俱乐部", "旁遮普足球俱乐部", "珂斯东孟加拉足球俱乐部",
													"皇家克什米尔足球俱乐部", "艾查威尔足球俱乐部", "莫亨巴根足球俱乐部",
													"蒂丁路运动联合会足球俱乐部", "钦奈城足球俱乐部"
												]
											}
										}
									}
								}
							}
						},
						"else": {
							"if": {
								"required": ["league_name"],
								"properties": {
									"league_name": {
										"contains": {
											"required": ["value"],
											"properties": {
												"value": {
													"enum": ["Japanischer Baseball",
														"Campionato di baseball giapponese",
														"Baseball japonais", "Béisbol japonés",
														"日本/セ・パリーグ・高校野球", "Japanese Baseball",
														"Japans honkbal", "japanese_baseball",
														"Japansk baseboll", "البيسبول الياباني",
														"Japoński baseball", "Beisebol japonês"
													]
												}
											}
										}
									}
								}
							},
							"then": {
								"properties": {
									"team_name": {
										"items": {
											"properties": {
												"value": {
													"enum": ["东京养乐多燕子队", "东北乐天金鹫队", "中日龙队", "北海道日本火腿斗士队",
														"千叶罗德海洋队", "埼玉西武狮队", "广岛东洋鲤鱼队", "横滨 DeNA 海湾之星队",
														"欧力士野牛队", "福冈软银鹰队", "读卖巨人队", "阪神虎队", "高中棒球队"
													]
												}
											}
										}
									}
								}
							},
							"else": {
								"if": {
									"required": ["league_name"],
									"properties": {
										"league_name": {
											"contains": {
												"required": ["value"],
												"properties": {
													"value": {
														"enum": ["イギリス/サッカー", "English Football",
															"Engels voetbal", "Football britannique",
															"كرة القدم الإنجليزية", "Angielska piłka nożna",
															"english_football", "English Soccer",
															"British Football", "Calcio inglese",
															"Futebol inglês", "Engelsk fotboll",
															"Englischer Fußball", "Fútbol inglés"
														]
													}
												}
											}
										}
									}
								},
								"then": {
									"properties": {
										"team_name": {
											"items": {
												"properties": {
													"value": {
														"enum": ["norwich", "QPR", "伊普斯维奇城足球俱乐部",
															"伍尔弗汉普顿流浪者足球俱乐部", "伯恩利足球俱乐部", "伯恩茅斯足球俱乐部",
															"伯明翰城足球俱乐部", "切尔西靴", "利兹联足球俱乐部", "利物浦",
															"南安普敦足球俱乐部", "博尔顿足球俱乐部", "卡迪夫城足球俱乐部", "卢顿足球俱乐部",
															"哈德斯菲尔德足球俱乐部", "埃弗顿足球俱乐部", "威根竞技足球俱乐部",
															"富勒姆足球俱乐部", "布伦特福德足球俱乐部", "布莱克本流浪者足球俱乐部",
															"布莱顿足球俱乐部", "布里斯托尔城足球俱乐部", "德比郡足球俱乐部",
															"托特纳姆热刺足球俱乐部", "斯托克城足球俱乐部", "斯旺西城足球俱乐部",
															"普利茅斯足球俱乐部", "普雷斯顿足球俱乐部", "曼城足球俱乐部",
															"曼彻斯特联足球俱乐部", "查尔顿竞技足球俱乐部", "桑德兰足球俱乐部",
															"水晶宫足球俱乐部", "沃特福德足球俱乐部", "米尔沃尔足球俱乐部",
															"米德尔斯堡足球俱乐部", "纽卡斯尔联足球俱乐部", "罗瑟勒姆足球俱乐部",
															"考文垂足球俱乐部", "莱斯特城足球俱乐部", "西布罗姆维奇足球俱乐部",
															"西汉姆联足球俱乐部", "诺丁汉森林足球俱乐部", "谢菲尔德星期三足球俱乐部",
															"谢菲尔德联足球俱乐部", "赫尔城足球俱乐部", "阅读", "阿斯顿维拉足球俱乐部",
															"阿森纳足球俱乐部"
														]
													}
												}
											}
										}
									}
								},
								"else": {
									"if": {
										"required": ["league_name"],
										"properties": {
											"league_name": {
												"contains": {
													"required": ["value"],
													"properties": {
														"value": {
															"enum": ["الاتحاد الدولي للمصارعة", "WUSA", "wusa"]
														}
													}
												}
											}
										}
									},
									"then": {
										"properties": {
											"team_name": {
												"items": {
													"properties": {
														"value": {
															"enum": ["WUSA"]
														}
													}
												}
											}
										}
									},
									"else": {
										"if": {
											"required": ["league_name"],
											"properties": {
												"league_name": {
													"contains": {
														"required": ["value"],
														"properties": {
															"value": {
																"enum": ["Première Ligue canadienne",
																	"Kanadensisk premiärliga",
																	"Canadian Premier League",
																	"Liga Premier Canadense",
																	"canadian_premier_league",
																	"Premiere League canadiense",
																	"Liga Premier canadiense",
																	"Premier League Kanada",
																	"Canadian Premiere League",
																	"الدوري الكندي الممتاز", "カナダ/プレミアリーグ",
																	"Kanadyjska Premiere League"
																]
															}
														}
													}
												}
											}
										},
										"then": {
											"properties": {
												"team_name": {
													"items": {
														"properties": {
															"value": {
																"enum": ["York9 FC", "卡尔加里骑兵足球俱乐部",
																	"哈利法克斯流浪者足球俱乐部", "哈密尔顿锻钢足球俱乐部",
																	"埃德蒙顿足球俱乐部", "太平洋足球俱乐部", "渥太华竞技",
																	"温哥华足球俱乐部", "温尼伯荣军足球俱乐部"
																]
															}
														}
													}
												}
											}
										},
										"else": {
											"if": {
												"required": ["league_name"],
												"properties": {
													"league_name": {
														"contains": {
															"required": ["value"],
															"properties": {
																"value": {
																	"enum": ["Fútbol español",
																		"Spanish Football",
																		"كرة القدم الاسبانية",
																		"Calcio spagnolo",
																		"Futebol espanhol",
																		"Football espagnol",
																		"spanish_football",
																		"Hiszpańska piłka nożna",
																		"Spanish Soccer",
																		"Spanischer Fußball",
																		"Spaans voetbal", "Spansk fotboll",
																		"スペイン/サッカー"
																	]
																}
															}
														}
													}
												}
											},
											"then": {
												"properties": {
													"team_name": {
														"items": {
															"properties": {
																"value": {
																	"enum": ["加的斯足球俱乐部", "努曼西亚足球俱乐部", "卢戈足球俱乐部",
																		"埃尔切足球俱乐部", "埃斯特雷马杜拉足球俱乐部",
																		"埃瓦尔足球俱乐部", "塞维利亚足球俱乐部",
																		"奥萨苏纳足球俱乐部", "富恩拉布拉达足球俱乐部",
																		"巴列卡诺足球俱乐部", "巴利亚多利德足球俱乐部",
																		"巴塞罗那足球俱乐部", "希洪竞技足球俱乐部",
																		"庞费拉迪纳足球俱乐部", "拉斯帕尔马斯足球俱乐部",
																		"拉科鲁尼亚足球俱乐部", "格拉纳达足球俱乐部",
																		"桑坦德竞技足球俱乐部", "比利亚雷亚尔足球俱乐部",
																		"毕尔巴鄂竞技足球俱乐部", "特内里费足球俱乐部",
																		"瓦伦西亚足球俱乐部", "皇家奥维耶多足球俱乐部",
																		"皇家社会足球俱乐部", "皇家萨拉戈萨足球俱乐部", "皇家马德里",
																		"米兰德斯足球俱乐部", "维戈塞尔塔足球俱乐部",
																		"莱万特足球俱乐部", "莱加内斯足球俱乐部",
																		"西班牙人足球俱乐部", "贝蒂斯塞维利亚足球俱乐部",
																		"赫塔菲足球俱乐部", "赫罗纳足球俱乐部",
																		"阿尔巴塞特足球俱乐部", "阿尔梅里亚足球俱乐部",
																		"阿尔科孔足球俱乐部", "阿拉维斯足球俱乐部",
																		"韦尔瓦足球俱乐部", "韦斯卡足球俱乐部",
																		"马德里竞技足球俱乐部", "马拉加足球俱乐部", "马略卡足球俱乐部"
																	]
																}
															}
														}
													}
												}
											},
											"else": {
												"if": {
													"required": ["league_name"],
													"properties": {
														"league_name": {
															"contains": {
																"required": ["value"],
																"properties": {
																	"value": {
																		"enum": ["Canadian Football League",
																			"Liga Canadense de Futebol",
																			"Kanadyjska Liga Piłkarska",
																			"canadian_football_league",
																			"Ligue canadienne de football",
																			"Kanadensisk fotbollsliga",
																			"الدوري الكندي لكرة القدم",
																			"Kanadische Fußballliga",
																			"カナディアン・フットボール・リーグ",
																			"Liga de fútbol canadiense"
																		]
																	}
																}
															}
														}
													}
												},
												"then": {
													"properties": {
														"team_name": {
															"items": {
																"properties": {
																	"value": {
																		"enum": ["不列颠哥伦比亚雄狮队", "卡尔加里牛仔队",
																			"埃德蒙顿足球队", "多伦多淘金人足球队",
																			"汉密尔顿虎猫队", "渥太华红黑队",
																			"温尼伯蓝色轰炸机足球队", "萨斯喀彻温省骑兵队",
																			"蒙特利尔云雀队"
																		]
																	}
																}
															}
														}
													}
												},
												"else": {
													"if": {
														"required": ["league_name"],
														"properties": {
															"league_name": {
																"contains": {
																	"required": ["value"],
																	"properties": {
																		"value": {
																			"enum": [
																				"دوري كرة السلة الأميركي للمحترفين",
																				"nba", "NBA"
																			]
																		}
																	}
																}
															}
														}
													},
													"then": {
														"properties": {
															"team_name": {
																"items": {
																	"properties": {
																		"value": {
																			"enum": ["丹佛掘金队", "亚特兰大老鹰队",
																				"休斯顿火箭队", "俄克拉荷马雷霆队",
																				"克利夫兰骑士队", "华盛顿奇才队",
																				"印第安纳步行者队", "圣地亚哥快船队",
																				"圣地亚哥火箭队", "圣安东尼奥马刺队",
																				"圣路易斯老鹰队", "圣路易斯轰炸机队",
																				"堪萨斯城国王队", "夏洛特黄蜂队",
																				"多伦多猛龙队", "奥兰多魔术队",
																				"孟菲斯灰熊队", "密尔沃基老鹰队",
																				"密尔沃基雄鹿队", "布鲁克林篮网队",
																				"底特律活塞队", "新奥尔良鹈鹕队",
																				"新泽西篮网队", "旧金山勇士队",
																				"明尼苏达森林狼队", "水牛城勇士队",
																				"波士顿凯尔特人队", "波特兰开拓者队",
																				"洛杉矶快船队", "洛杉矶湖人队", "犹他爵士队",
																				"纽约尼克斯队", "罗切斯特皇家队",
																				"芝加哥公牛队", "芝加哥牡鹿队",
																				"芝加哥西风队", "菲尼克斯太阳队",
																				"萨克拉门托国王队", "西拉丘斯国民队",
																				"西雅图超音速队", "费城76人队",
																				"达拉斯独行侠队", "迈阿密热火队",
																				"金州勇士队", "韦恩堡活塞队"
																			]
																		}
																	}
																}
															}
														}
													},
													"else": {
														"if": {
															"required": ["league_name"],
															"properties": {
																"league_name": {
																	"contains": {
																		"required": ["value"],
																		"properties": {
																			"value": {
																				"enum": ["Minor League Honkbal",
																					"minor_league_baseball",
																					"Béisbol de ligas menores",
																					"Minor League Baseball",
																					"Liga Menor de Beisebol",
																					"Ligue mineure de baseball",
																					"アメリカ/マイナーリーグ",
																					"دوري البيسبول المصغر"
																				]
																			}
																		}
																	}
																}
															}
														},
														"then": {
															"properties": {
																"team_name": {
																	"items": {
																		"properties": {
																			"value": {
																				"enum": ["三城谷猫队", "三市尘埃魔鬼队",
																					"丹维尔勇士队", "亨茨维尔星队",
																					"代托纳小熊队", "伊利海狼队",
																					"伊利莎白顿双城队", "伦秋库卡蒙佳地震队",
																					"伯明翰男爵队", "伯灵顿皇家队",
																					"伯灵顿蜜蜂队", "但尼丁蓝鸟队",
																					"佛蒙特湖怪兽队", "俄克拉荷马城红鹰队",
																					"克林顿伐木王队", "兰开斯特喷射鹰队",
																					"兰辛螺帽队", "内陆帝国 66 人队",
																					"凯恩县美洲狮队", "列克星敦传奇队",
																					"华盛顿国民队", "南弯银鹰队",
																					"博伊西老鹰队", "卡斯柏小鬼队",
																					"卡罗莱纳泥鳅队",
																					"印第安纳波利斯印地安人队",
																					"史坦顿岛洋基队", "哈德逊河谷叛徒队",
																					"哈里斯堡参议员队", "哥伦布快船队",
																					"圆石城特快车队", "圣何塞巨人队",
																					"圣安东尼奥使命队", "圣露西大都会队",
																					"坎纳波利斯大炮队", "坦帕洋基队",
																					"塔可马雨人队", "塔尔萨钻探者队",
																					"塞勒姆凯泽火山队", "夏洛特石蟹队",
																					"夏洛特骑士队", "大湖潜鸟队",
																					"大瀑布航海者队", "奎德城河盗队",
																					"奥古斯塔格林杰克斯队", "奥本达博岱队",
																					"奥格登猛龙队", "奥马哈风暴追赶者队",
																					"威尔明顿蓝岩队", "威廉波特横锯队",
																					"威斯康辛木纹响尾蛇队", "孟德斯托坚果队",
																					"孟菲斯红鸟队", "宾厄姆顿大都会队",
																					"密苏里州鱼鹰队", "密西西比勇士队",
																					"尤金绿宝石队", "州立大学尖峰队",
																					"巴塔维亚脏狗队", "布雷瓦德县海牛队",
																					"布雷登顿掠夺者队", "布鲁克林飓风队",
																					"希科里淡水龙虾队", "康涅狄格老虎队",
																					"弗里斯科游骑兵队", "弗雷德里克钥匙队",
																					"弗雷斯诺灰熊队", "德玛瓦水鸟队",
																					"德顿龙队", "托莱多泥母鸡队",
																					"拉斯维加斯飞行者队",
																					"斯克兰顿/威尔克斯-巴里洋基队",
																					"斯托克顿港口队", "斯普林菲尔德红雀队",
																					"斯波坎印第安人队", "新不列颠石猫队",
																					"新奥尔良圣徒队", "新罕布什尔捕鱼猫队",
																					"普拉斯基洋基队", "普林斯顿射线队",
																					"木星锤头队", "杰克逊维尔太阳队",
																					"林奇堡山猫队", "查塔努加观景峰队",
																					"查尔斯顿河狗队", "格威内特勇士队",
																					"格林斯波罗蚱蜢队", "格林维尔大道队",
																					"格林维尔太空人队", "棕榈滩红雀队",
																					"欧仁猫头鹰队", "比林斯野马队",
																					"水牛城野牛队", "沙瓦那沙虫队",
																					"波塔基特红袜队", "波士顿红袜队",
																					"波托马克国民队", "波特兰海狗队",
																					"波特兰海狸队", "泽西岛海岸蓝爪队",
																					"洋杉激流麦子队", "洛威旋球队",
																					"海伦娜酿酒人队", "清水长尾鲛队",
																					"温哥华加拿大人队", "温斯顿塞勒姆冲刺队",
																					"爱荷华小熊队", "爱达荷瀑布鹌鹑队",
																					"特伦顿雷霆队", "理海谷野猪队",
																					"田纳西斯摩基队", "皮奥里亚酋长队",
																					"盐湖城蜜蜂队", "科珀斯克里斯蒂铁钩队",
																					"科罗拉多斯普林斯天空袜队",
																					"米德兰摇滚猎犬队", "约翰逊城红衣主教队",
																					"纳什维尔天籁队", "维萨利亚拉希德队",
																					"罗切斯特红翼队", "罗马勇士队",
																					"艾佛列特绿袜队", "艾克隆航空队",
																					"艾森豪湖风暴队", "芝加哥白袜队",
																					"莫比尔海湾熊队", "莱克兰飞虎队",
																					"莱克郡队长队", "萨克拉门托河猫队",
																					"蒙哥马利饼干队", "蓝田金莺队",
																					"西北阿肯色自然队", "西密歇根白帽队",
																					"西弗吉尼亚力量队", "西田纳西钻石工队",
																					"詹姆斯敦干扰者队", "诺福克潮汐队",
																					"贝克斯菲尔德火焰队", "贝洛伊特鲷鱼队",
																					"路易斯维尔蝙蝠队", "达勒姆公牛队",
																					"迈尔斯堡奇迹队", "里士满飞鼠队",
																					"金士顿印第安人队", "金斯波特大都会队",
																					"阿什维尔旅行者队", "阿伯丁铁鸟队",
																					"阿尔伯克基同位素队", "阿尔图纳曲线队",
																					"阿肯色旅行者队", "雅吉玛熊队",
																					"雪城酋长队", "雷丁费城人队",
																					"雷诺王牌队", "韦恩堡锡帽队",
																					"马霍宁谷拳击手队", "高沙漠小牛队",
																					"鲍伊贝索克斯队", "鲍灵格林热棒队",
																					"黑格斯敦太阳队", "默特尔比奇鹈鹕队"
																				]
																			}
																		}
																	}
																}
															}
														},
														"else": {
															"if": {
																"required": ["league_name"],
																"properties": {
																	"league_name": {
																		"contains": {
																			"required": ["value"],
																			"properties": {
																				"value": {
																					"enum": [
																						"دوري الهوكي الوطني",
																						"nhl", "NHL",
																						"Ligue nationale de hockey"
																					]
																				}
																			}
																		}
																	}
																}
															},
															"then": {
																"properties": {
																	"team_name": {
																		"items": {
																			"properties": {
																				"value": {
																					"enum": ["亚利桑那郊狼队",
																						"亚特兰大火焰队",
																						"亚特兰大鸫鸟队",
																						"佛罗里达美洲豹队",
																						"克利夫兰男爵队", "加州海豹队",
																						"匹兹堡企鹅队", "华盛顿首都队",
																						"卡尔加里火焰队",
																						"卡罗莱纳飓风队",
																						"哈特福德捕鲸人队",
																						"哥伦布蓝衣队", "圣何塞鲨鱼队",
																						"圣路易斯老鹰队",
																						"圣路易斯蓝调队", "坦帕湾闪电队",
																						"埃德蒙顿油人队",
																						"堪萨斯城童子军队",
																						"多伦多圣帕茨队", "多伦多枫叶队",
																						"多伦多竞技场队", "安纳海姆鸭队",
																						"底特律猎鹰队", "底特律红翼队",
																						"底特律美洲狮队", "新泽西魔鬼队",
																						"明尼苏达北极星队",
																						"明尼苏达荒野队", "水牛城军刀",
																						"汉密尔顿老虎队", "波士顿棕熊队",
																						"洛杉矶国王队", "渥太华参议员队",
																						"温哥华加人队", "温尼伯喷射机队",
																						"科罗拉多雪崩队",
																						"纳什维尔掠夺者队", "纽约岛民队",
																						"纽约游骑兵队", "纽约美国人队",
																						"维加斯黄金骑士队",
																						"芝加哥黑鹰队",
																						"蒙特利尔加拿大人队",
																						"蒙特利尔流浪者队",
																						"蒙特利尔褐红队", "西雅图海妖队",
																						"费城贵格会队", "费城飞人队",
																						"达拉斯星队"
																					]
																				}
																			}
																		}
																	}
																}
															},
															"else": {
																"if": {
																	"required": ["league_name"],
																	"properties": {
																		"league_name": {
																			"contains": {
																				"required": ["value"],
																				"properties": {
																					"value": {
																						"enum": [
																							"European Basketball",
																							"Europees basketbal",
																							"كرة السلة الأوروبية",
																							"european_basketball",
																							"Baloncesto europeo",
																							"Europeisk basket",
																							"Lega europea di pallacanestro",
																							"バスケットボール ユーロリーグ",
																							"Basquete europeu",
																							"Europejska koszykówka",
																							"Basketball européen",
																							"Europäischer Basketball"
																						]
																					}
																				}
																			}
																		}
																	}
																},
																"then": {
																	"properties": {
																		"team_name": {
																			"items": {
																				"properties": {
																					"value": {
																						"enum": ["ASVEL篮球队",
																							"奥林匹亚科斯篮球俱乐部",
																							"安纳托利亚艾菲斯伊斯坦布尔篮球队",
																							"巴塞罗那篮球俱乐部",
																							"巴斯干尼亚篮球队",
																							"希姆基莫斯科地区篮球队",
																							"帕纳辛奈克斯篮球俱乐部",
																							"拜仁慕尼黑篮球俱乐部",
																							"柏林欧绿保篮球俱乐部",
																							"柳别尔齐胜利篮球俱乐部",
																							"特拉维夫马卡比篮球俱乐部",
																							"瓦伦西亚篮球俱乐部",
																							"皇家马德里篮球队",
																							"米兰奥林匹亚篮球俱乐部",
																							"莫斯科中央陆军篮球俱乐部",
																							"萨拉基利斯篮球俱乐部",
																							"贝尔格莱德红星篮球俱乐部",
																							"费内巴切伊斯坦布尔篮球俱乐部"
																						]
																					}
																				}
																			}
																		}
																	}
																},
																"else": {
																	"if": {
																		"required": ["league_name"],
																		"properties": {
																			"league_name": {
																				"contains": {
																					"required": ["value"],
																					"properties": {
																						"value": {
																							"enum": [
																								"Liga Premier de Badminton",
																								"インド/バドミントンリーグ",
																								"Premier Badminton League",
																								"premier_badminton_league",
																								"Liga Premier de Bádminton",
																								"Premier Badminton Ligan",
																								"الدوري الممتاز لكرة الريشة"
																							]
																						}
																					}
																				}
																			}
																		}
																	},
																	"then": {
																		"properties": {
																			"team_name": {
																				"items": {
																					"properties": {
																						"value": {
																							"enum": ["东北勇士队",
																								"孟买火箭队",
																								"浦那 7 王牌队",
																								"海得拉巴猎人队",
																								"班加罗尔猛龙队",
																								"钦奈超级星队",
																								"阿瓦德勇士队"
																							]
																						}
																					}
																				}
																			}
																		}
																	},
																	"else": {
																		"if": {
																			"required": ["league_name"],
																			"properties": {
																				"league_name": {
																					"contains": {
																						"required": ["value"],
																						"properties": {
																							"value": {
																								"enum": [
																									"Otros equipos de fútbol",
																									"Other Soccer Leagues",
																									"other_football_leagues",
																									"サッカーリーグ/その他",
																									"Otras ligas de fútbol",
																									"Inne ligi piłkarskie",
																									"Andere voetbalcompetities",
																									"Autres équipes de football",
																									"Mehr Fußballmannschaften",
																									"More Football Leagues",
																									"Outras ligas de futebol",
																									"بطولات كرة القدم الأخرى",
																									"Altre squadre di calcio",
																									"Andra fotbollsligor"
																								]
																							}
																						}
																					}
																				}
																			}
																		},
																		"then": {
																			"properties": {
																				"team_name": {
																					"items": {
																						"properties": {
																							"value": {
																								"enum": [
																									"加拉塔萨雷足球俱乐部",
																									"博卡青年队",
																									"图拉兵工厂足球俱乐部",
																									"圣保罗足球俱乐部",
																									"巴塞尔足球俱乐部",
																									"河床竞技俱乐部",
																									"河床足球俱乐部",
																									"科林蒂安保利斯塔体育会",
																									"罗马体育俱乐部",
																									"莫斯科斯巴达克足球俱乐部",
																									"萨兰迪兵工厂足球俱乐部"
																								]
																							}
																						}
																					}
																				}
																			}
																		},
																		"else": {
																			"if": {
																				"required": ["league_name"],
																				"properties": {
																					"league_name": {
																						"contains": {
																							"required": [
																								"value"],
																							"properties": {
																								"value": {
																									"enum": [
																										"ポルトガル/サッカー",
																										"Portuguese Football",
																										"Futebol português",
																										"Fútbol portugués",
																										"Football portugais",
																										"Portugees voetbal",
																										"Portugiesischer Fußball",
																										"portuguese_football",
																										"Portugisisk fotboll",
																										"Portuguese Soccer",
																										"Portugalska piłka nożna",
																										"كرة القدم البرتغالية",
																										"Campionato di calcio portoghese"
																									]
																								}
																							}
																						}
																					}
																				}
																			},
																			"then": {
																				"properties": {
																					"team_name": {
																						"items": {
																							"properties": {
																								"value": {
																									"enum": [
																										"体育足球俱乐部",
																										"博阿维斯塔足球俱乐部",
																										"卡萨皮亚",
																										"吉尔维森特足球俱乐部",
																										"吉马良斯维多利亚足球俱乐部",
																										"圣克拉拉足球俱乐部",
																										"埃斯托里尔",
																										"塞图巴尔维多利亚足球俱乐部",
																										"布拉加竞技俱乐部",
																										"本菲卡足球俱乐部",
																										"沙维什",
																										"法伦斯",
																										"法马利康足球俱乐部",
																										"波尔图足球俱乐部",
																										"波蒂莫嫩塞足球俱乐部",
																										"艾维斯体育俱乐部",
																										"莫雷伦斯足球俱乐部",
																										"贝伦人足球俱乐部",
																										"费雷拉",
																										"费雷拉柏苏斯足球俱乐部",
																										"通德拉足球俱乐部",
																										"里奥艾维足球俱乐部",
																										"阿罗卡",
																										"马里迪莫体育俱乐部"
																									]
																								}
																							}
																						}
																					}
																				}
																			},
																			"else": {
																				"if": {
																					"required": ["league_name"],
																					"properties": {
																						"league_name": {
																							"contains": {
																								"required": [
																									"value"
																								],
																								"properties": {
																									"value": {
																										"enum": [
																											"الدوري الأمريكي لكرة القدم",
																											"mls",
																											"MLS"
																										]
																									}
																								}
																							}
																						}
																					}
																				},
																				"then": {
																					"properties": {
																						"team_name": {
																							"items": {
																								"properties": {
																									"value": {
																										"enum": [
																											"C.D.美国芝华士足球俱乐部",
																											"DC联队",
																											"亚特兰大联足球俱乐部",
																											"休斯顿迪纳摩队",
																											"哥伦布机员足球俱乐部",
																											"圣何塞地震队",
																											"圣路易斯城",
																											"堪萨斯城体育会队",
																											"多伦多足球俱乐部",
																											"奥兰多城队",
																											"奥斯汀足球俱乐部",
																											"新英格兰革命足球俱乐部",
																											"明尼苏达联",
																											"波特兰伐木者足球俱乐部",
																											"洛杉矶足球俱乐部",
																											"洛杉矶银河足球俱乐部",
																											"温哥华白浪足球俱乐部",
																											"皇家盐湖城队",
																											"科罗拉多急流队",
																											"纳什维尔足球俱乐部",
																											"纽约FC",
																											"纽约红牛队",
																											"芝加哥火焰队",
																											"蒙特利尔冲击",
																											"西雅图海湾人足球俱乐部",
																											"费城联足球俱乐部",
																											"辛辛那提足球俱乐部",
																											"达拉斯足球俱乐部",
																											"迈阿密国际足球俱乐部"
																										]
																									}
																								}
																							}
																						}
																					}
																				},
																				"else": {
																					"if": {
																						"required": [
																							"league_name"
																						],
																						"properties": {
																							"league_name": {
																								"contains": {
																									"required": [
																										"value"
																									],
																									"properties": {
																										"value": {
																											"enum": [
																												"ncaa",
																												"دوري الجامعات الأمريكية لكرة السلة",
																												"NCAA"
																											]
																										}
																									}
																								}
																							}
																						}
																					},
																					"then": {
																						"properties": {
																							"team_name": {
																								"items": {
																									"properties": {
																										"value": {
																											"enum": [
																												"东伊利诺伊大学黑豹队",
																												"东北伊利诺伊州金鹰队",
																												"东北大学哈士奇队",
																												"东华盛顿大学老鹰队",
																												"东南密苏里州印第安人队",
																												"东南密苏里州州立大学红鹰队",
																												"东卡罗来纳大学海盗队",
																												"东密歇根大学老鹰队",
																												"东田纳西州州立大学海盗队",
																												"东肯塔基大学上校队",
																												"中佛罗里达大学金骑士队",
																												"中密歇根大学奇普瓦斯队",
																												"中康涅狄格大学蓝魔队",
																												"中田纳西州立大学蓝色突袭者队",
																												"丹佛大学先驱者队",
																												"乔治亚南方大学鹰队",
																												"乔治亚斗牛犬队",
																												"乔治亚理工大黄蜂队",
																												"乔治华盛顿殖民者队",
																												"乔治城大学惊叹队",
																												"乔治梅森大学爱国者队",
																												"亚利桑那大学野猫队",
																												"亚利桑那州立大学太阳魔队",
																												"伊利诺伊大学伊利尼队",
																												"伊利诺伊大学芝加哥分校火焰队",
																												"伊利诺伊州立大学红雀队",
																												"伊隆大学凤凰队",
																												"休斯顿大学美洲狮队",
																												"休斯顿浸会大学哈士奇队",
																												"佐治亚州立大学黑豹队",
																												"佛罗里达农工大学响尾蛇队",
																												"佛罗里达国际大学金豹队",
																												"佛罗里达国际大学黑豹队",
																												"佛罗里达大学短吻鳄队",
																												"佛罗里达大西洋猫头鹰队",
																												"佛罗里达州立大学塞米诺尔队",
																												"佛罗里达海湾海岸鹰队",
																												"佛蒙特大山猫队",
																												"佩珀代因大学波浪队",
																												"俄亥俄大学山猫队",
																												"俄亥俄州立大学七叶树队",
																												"俄克拉荷马大学捷足者队",
																												"俄克拉荷马州立大学牛仔队",
																												"俄勒冈大学鸭子队",
																												"俄勒冈州立大学海狸队",
																												"克利夫兰州立维京人队",
																												"克莱姆森大学老虎队",
																												"克赖顿大学蓝鸟队",
																												"内华达大学拉斯维加斯分校叛逆者队",
																												"内华达大学狼群队",
																												"内布拉斯加大学剥玉米人队",
																												"冈萨加大学斗牛犬队",
																												"利伯缇大学火焰队",
																												"利哈伊山大学老鹰队",
																												"加州大学圣巴巴拉分校高乔人队",
																												"加州大学尔湾分校食蚁兽队",
																												"加州大学戴维斯分校队",
																												"加州大学河滨高地人队",
																												"加州大学洛杉矶分校棕熊队",
																												"加州州立大学北岭斗牛士队",
																												"加州州立大学富勒顿泰坦队",
																												"加州州立大学萨克拉门托黄蜂队",
																												"加州州立大学贝克斯菲尔德路跑者队",
																												"加州浸会大学兰瑟斯队",
																												"加州理工大学野马队",
																												"加州金熊队",
																												"加德纳韦伯鲁宁斗牛犬队",
																												"北亚利桑那大学伐木工人队",
																												"北伊利诺斯大学哈士奇队",
																												"北佛罗里达大学鱼鹰队",
																												"北卡罗来纳中央老鹰队",
																												"北卡罗来纳农工大学队",
																												"北卡罗来纳大学夏洛特分校 49 人队",
																												"北卡罗来纳大学威尔明顿分校海鹰队",
																												"北卡罗来纳大学格林斯伯勒分校斯巴达人队",
																												"北卡罗来纳州立大学狼群队",
																												"北卡罗来纳州阿什维尔斗牛犬队",
																												"北卡罗莱纳大学焦油踵队",
																												"北德克萨斯大学蛮横格林队",
																												"北爱荷华大学黑豹队",
																												"北科罗拉多熊队",
																												"北肯塔基大学挪威人队",
																												"北达科他大学战斗鹰队",
																												"北达科他大学斗鹰队",
																												"北达科他大学队",
																												"北达科他州立大学野牛队",
																												"北阿拉巴马狮子队",
																												"匹兹堡大学黑豹队",
																												"华盛顿大学哈士奇队",
																												"华盛顿州立大学美洲狮队",
																												"南伊利诺伊大学萨路基猎犬队",
																												"南伊利诺大学爱德华城美洲狮队",
																												"南佛罗里达大学公牛队",
																												"南加州大学北部分校斯巴达人队",
																												"南加州大学特洛伊人队",
																												"南卡罗来纳州大学斗鸡队",
																												"南卡罗来纳州立大学斗牛犬队",
																												"南卫理工会大学野马队",
																												"南密西西比大学金鹰队",
																												"南方大学美洲虎队",
																												"南犹他大学雷鸟队",
																												"南达科他大学郊狼队",
																												"南达科他州立大学野兔队",
																												"南阿拉巴马大学美洲虎队",
																												"博伊西州立大学野马队",
																												"卡尼修斯学院金狮鹫兽队",
																												"卡罗莱纳海岸大学唱诗班队",
																												"印第安纳大学山地人队",
																												"印第安纳大学美洲虎队",
																												"印第安纳大学－普渡大学韦恩堡分校乳齿象队",
																												"印第安纳州梧桐队",
																												"哈佛大学深红队",
																												"哈特福德大学老鹰队",
																												"哥伦比亚大学狮子队",
																												"圣何塞州立大学斯巴达人队",
																												"圣克拉拉野马队",
																												"圣十字学院十字军队",
																												"圣博纳旺蒂尔邦尼队",
																												"圣地亚哥大学斗牛士队",
																												"圣地亚哥州立大学阿兹台克人队",
																												"圣弗朗西斯大学（宾夕法尼亚州）红色闪光队",
																												"圣弗朗西斯学院（纽约）梗犬队",
																												"圣弗朗西斯布鲁克林梗队",
																												"圣彼得大学孔雀队",
																												"圣心先锋队",
																												"圣母大学爱尔兰战士队",
																												"圣玛丽大学盖尔人队",
																												"圣玛丽学院响尾蛇队",
																												"圣玛丽山大学登山者队",
																												"圣约瑟夫老鹰队",
																												"圣约翰红色风暴队",
																												"圣路易斯大学比利肯斯队",
																												"圣道学院红雀队",
																												"坎贝尔大学战斗骆驼队",
																												"埃文斯维尔紫色王牌队",
																												"堪萨斯大学杰鹰队",
																												"堪萨斯州立大学野猫队",
																												"塔尔萨大学金色飓风队",
																												"塞顿霍尔大学海盗队",
																												"夏威夷大学彩虹勇士队",
																												"夏洛特大学 49 人队",
																												"大峡谷大学羚羊队",
																												"天普大学猫头鹰队",
																												"太平洋大学拳击手队",
																												"太平洋大学老虎队",
																												"奥克兰大学金熊队",
																												"奥尔巴尼大丹尼斯队",
																												"奥尔康州立勇士队",
																												"奥斯汀皮埃州长队",
																												"奥本大学老虎队",
																												"奥罗尔罗伯茨大学金鹰队",
																												"奥马哈小牛队",
																												"威克森林大学恶魔指挥官队",
																												"威奇托州立大学震撼者队",
																												"威廉玛丽学院部落队",
																												"威斯康星大学绿湾凤凰队",
																												"威斯康星密尔沃基黑豹队",
																												"威斯康星獾队",
																												"孟菲斯老虎队",
																												"宾夕法尼亚大学贵格会队",
																												"宾州州立大学尼塔尼雄狮队",
																												"宾汉顿大学熊狸队",
																												"密歇根大学狼獾队",
																												"密歇根州立斯巴达人队",
																												"密苏里大学堪萨斯城分校袋鼠队",
																												"密苏里大学老虎队",
																												"密西西比大学反叛者队",
																												"密西西比大学叛逆者队",
																												"密西西比州立大学斗牛犬队",
																												"密西西比河谷州三角洲魔鬼队",
																												"尼亚加拉大学紫鹰队",
																												"尼科尔斯州立大学上校队",
																												"巴克内尔大学野牛队",
																												"巴特勒大学斗牛犬队",
																												"布拉德利勇士队",
																												"布朗大学熊队",
																												"布法罗大学公牛队",
																												"布莱恩特大学斗牛犬队",
																												"底特律大学泰坦队",
																												"康奈尔大红队",
																												"康涅狄格大学哈士奇队",
																												"康涅狄格州中部蓝魔队",
																												"弗吉尼亚军事学院军校生队",
																												"弗吉尼亚大学骑士队",
																												"弗吉尼亚理工大学霍奇队",
																												"弗吉尼亚联邦大学公羊队",
																												"弗曼大学圣骑士队",
																												"弗雷斯诺州立斗牛犬队",
																												"得州农工大学农夫队",
																												"德保罗大学蓝色恶魔队",
																												"德克萨斯农工大学科普斯克里斯蒂岛民队",
																												"德克萨斯南方大学老虎队",
																												"德克萨斯大学埃尔帕索矿工队",
																												"德克萨斯大学泛美分校野马队",
																												"德克萨斯大学长角牛队",
																												"德克萨斯大学阿灵顿分校小牛队",
																												"德克萨斯州立大学山猫队",
																												"德克萨斯理工大学红色突袭者队",
																												"德克萨斯里奥格兰德河谷大学牛仔队",
																												"德州基督教大学角蛙队",
																												"德州大学圣安东尼奥分校路跑者队",
																												"德雷克大学斗牛犬队",
																												"德雷塞尔大学龙队",
																												"怀俄明大学牛仔队",
																												"戴维森学院野猫队",
																												"戴顿大学飞人队",
																												"托莱多火箭队",
																												"扬斯敦州立大学企鹅队",
																												"拉德福德大学高地人队",
																												"拉斐特学院花豹队",
																												"拉萨尔大学探险者队",
																												"拉马尔大学红雀队",
																												"摩根州立大学熊队",
																												"斯坦福大学深红队",
																												"斯泰森大学制帽人队",
																												"斯蒂芬奥斯汀州立大学伐木工人队",
																												"新墨西哥大学罗伯斯队",
																												"新墨西哥州立大学阿吉斯队",
																												"新奥尔良大学私掠船队",
																												"新泽西理工学院高地人队",
																												"新罕布什尔大学野猫队",
																												"旧金山大学唐斯队",
																												"昆尼匹克大学山猫队",
																												"明尼苏达大学金地鼠队",
																												"普林斯顿大学老虎队",
																												"普渡大学锅炉工队",
																												"普罗维登斯学院修道士队",
																												"曼哈顿学院贾斯珀队",
																												"朗伍德大学枪骑兵队",
																												"杜克大学蓝魔队",
																												"杜兰大学绿浪队",
																												"杨百翰大学美洲狮队",
																												"杰克逊州立大学老虎队",
																												"杰克逊维尔州立大学斗鸡队",
																												"杰克逊维尔海豚队",
																												"查尔斯顿南方海盗队",
																												"查尔斯顿学院美洲狮队",
																												"格兰布林州立大学老虎队",
																												"汉普顿大学海盗队",
																												"沃福德学院小猎狗队",
																												"波士顿大学梗犬队",
																												"波士顿学院鹰队",
																												"波尔州立大学红雀队",
																												"波特兰大学飞行员队",
																												"波特兰州立大学维京人队",
																												"泽维尔大学火枪手队",
																												"洛约拉马利蒙特大学雄狮队",
																												"海军学院见习官队",
																												"海波因特大学黑豹队",
																												"温斯罗普大学鹰队",
																												"爱奥那学院盖尔斯队",
																												"爱荷华大学鹰眼队",
																												"爱荷华州立大学旋风队",
																												"爱达荷大学破坏者队",
																												"爱达荷州立大学孟加拉虎队",
																												"特拉华大学战斗蓝母鸡队",
																												"特拉华州立大学黄蜂队",
																												"特洛伊州立大学特洛伊人队",
																												"犹他大学犹特队",
																												"犹他州立大学农人队",
																												"犹他谷大学狼獾队",
																												"瓦尔帕莱索大学十字军队",
																												"瓦格纳学院海鹰队",
																												"田纳西大学志愿者队",
																												"田纳西大学查塔努加分校莫奇队",
																												"田纳西大学科技金鹰队",
																												"田纳西大学马丁分校天鹰队",
																												"田纳西州立大学老虎队",
																												"白求恩库克曼大学野猫队",
																												"百年绅士队",
																												"石溪大学海狼队",
																												"福特汉姆大学公羊队",
																												"科尔盖特大学突袭者队",
																												"科平州立老鹰队",
																												"科罗拉多大学老虎队",
																												"科罗拉多州立大学公羊队",
																												"科罗拉多水牛队",
																												"空军猎鹰队",
																												"立普斯康大学野牛队",
																												"纽约大学山猫队",
																												"纽约大学紫罗兰队",
																												"纽约理工学院熊队",
																												"维拉诺瓦野猫队",
																												"缅因大学黑熊队",
																												"罗伯特莫里斯殖民地队",
																												"罗德岛大学公羊队",
																												"罗格斯红衣骑士队",
																												"美国大学老鹰队",
																												"老道明大学君主队",
																												"耶鲁大学斗牛犬队",
																												"肯塔基大学野猫队",
																												"肯尼索州立猫头鹰队",
																												"肯特州立大学金色光芒队",
																												"芝加哥州立美洲狮队",
																												"芝加哥洛约拉大学漫步者队",
																												"范德比尔特大学船长队",
																												"草原风光农工大学美洲豹队",
																												"莫尔黑德州立大学老鹰队",
																												"莱德大学野马队",
																												"莱斯大学猫头鹰队",
																												"莱特州立大学突袭者队",
																												"菲尔莱狄更斯大学魔鬼队",
																												"萨凡纳州立大学老虎队",
																												"萨姆休斯顿州立大学熊狸队",
																												"萨姆福德大学斗牛犬队",
																												"蒙大拿州立大学山猫队",
																												"蒙大拿灰熊队",
																												"蒙茅斯大学老鹰队",
																												"西伊利诺伊大学海军陆战队员队",
																												"西北大学野猫队",
																												"西北州立大学恶魔队",
																												"西南密苏里州立大学熊队",
																												"西卡罗莱纳大学大山猫队",
																												"西密歇根大学野马队",
																												"西弗吉尼亚大学登山人队",
																												"西肯塔基大学山顶队",
																												"西雅图大学红鹰队",
																												"要塞军事学院斗牛犬队",
																												"詹姆斯麦迪逊大学公爵队",
																												"诺福克州立大学斯巴达人队",
																												"贝勒大学熊队",
																												"贝尔蒙特大学棕熊队",
																												"贝茨山猫队",
																												"费尔利狄金森大学骑士队",
																												"费尔菲尔德大学雄鹿队",
																												"路易斯安那东南狮队",
																												"路易斯安那大学拉法叶拉金卡津队",
																												"路易斯安那大学门罗分校战鹰队",
																												"路易斯安那州立大学老虎队",
																												"路易斯安那州门罗大学印第安人队",
																												"路易斯安那理工大学牛头犬队",
																												"路易斯维尔大学红雀队",
																												"辛辛那提大学熊狸队",
																												"达拉斯浸会大学爱国者队",
																												"达特茅斯大绿队",
																												"迈阿密大学飓风队",
																												"迈阿密大学（俄亥俄州）红鹰队",
																												"迪尤肯大学公爵队",
																												"里士满大学蜘蛛队",
																												"锡耶纳学院圣徒队",
																												"长岛大学黑鸟队",
																												"长滩州立大学 49 人队",
																												"长滩州立大学鲨鱼队",
																												"长老会学院蓝色软管队",
																												"阿克伦大学拉链靴队",
																												"阿巴拉契亚州登山者队",
																												"阿拉巴马农工大学斗牛犬队",
																												"阿拉巴马大学伯明翰分校开拓者队",
																												"阿拉巴马州立大学黄蜂队",
																												"阿拉巴马赤潮队",
																												"阿比林基督教大学野猫队",
																												"阿肯色大学松树壁分校金狮队",
																												"阿肯色大学野猪队",
																												"阿肯色州中部熊队",
																												"阿肯色州小石城特洛伊队",
																												"阿肯色州立大学印地安人队",
																												"阿肯色州立大学红狼队",
																												"陆军黑骑士队",
																												"陶森大学老虎队",
																												"雪城大学橙人队",
																												"霍华德大学野牛队",
																												"霍夫斯特拉大学骄傲队",
																												"霍夫斯特拉飞行荷兰人队",
																												"韦伯州立野猫队",
																												"韦恩堡乳齿象队",
																												"马奎特大学金鹰队",
																												"马歇尔大学雷霆追风队",
																												"马萨诸塞大学民兵队",
																												"马里兰大学东岸分校战鹰队",
																												"马里兰大学巴尔的摩县猎犬队",
																												"马里兰大学水龟队",
																												"马里兰洛约拉大学灰狗队",
																												"马里斯特学院红狐狸队",
																												"鲍灵格林猎鹰队",
																												"麦克尼斯州立大学牛仔队",
																												"麻省大学罗威尔分校河鹰队",
																												"默瑟大学熊队",
																												"默里州立大学赛车手队"
																											]
																										}
																									}
																								}
																							}
																						}
																					},
																					"else": {
																						"if": {
																							"required": [
																								"league_name"
																							],
																							"properties": {
																								"league_name": {
																									"contains": {
																										"required": [
																											"value"
																										],
																										"properties": {
																											"value": {
																												"enum": [
																													"Campionato mondiale di calcio",
																													"World Cup Football",
																													"Wereldkampioenschap voetbal",
																													"Copa do Mundo de Futebol",
																													"World Cup Soccer",
																													"Mistrzostwa Świata w Piłce Nożnej",
																													"VM-fotboll",
																													"Fútbol Copa del Mundo",
																													"Fußballweltmeisterschaft",
																													"كأس العالم لكرة القدم",
																													"FIFAワールドカップ",
																													"Coupe du monde de football",
																													"Copa mundial de fútbol",
																													"world_cup_football"
																												]
																											}
																										}
																									}
																								}
																							}
																						},
																						"then": {
																							"properties": {
																								"team_name": {
																									"items": {
																										"properties": {
																											"value": {
																												"enum": [
																													"中国",
																													"丹麦",
																													"乌克兰",
																													"乌拉圭",
																													"伊朗",
																													"俄罗斯",
																													"保加利亚",
																													"克罗地亚",
																													"加拿大",
																													"加纳",
																													"南斯拉夫",
																													"南非",
																													"卡塔尔",
																													"厄瓜多尔",
																													"哥伦比亚",
																													"哥斯达黎加",
																													"喀麦隆",
																													"塞尔维亚和黑山",
																													"墨西哥",
																													"多哥",
																													"奥地利",
																													"委内瑞拉",
																													"威尔士",
																													"安哥拉",
																													"尼日利亚",
																													"巴拉圭",
																													"巴西",
																													"希腊",
																													"德国",
																													"意大利",
																													"挪威",
																													"捷克共和国",
																													"摩洛哥",
																													"斯洛文尼亚",
																													"日本",
																													"智利",
																													"比利时",
																													"沙特阿拉伯",
																													"法国",
																													"波兰",
																													"澳大利亚",
																													"火鸡肉",
																													"爱尔兰",
																													"牙买加",
																													"特立尼达和多巴哥",
																													"玻利维亚",
																													"瑞典",
																													"瑞士",
																													"科特迪瓦",
																													"突尼斯",
																													"罗马尼亚",
																													"美国",
																													"苏格兰",
																													"英国",
																													"荷兰",
																													"葡萄牙",
																													"西班牙",
																													"金翼鹦鹉",
																													"阿根廷",
																													"韩国"
																												]
																											}
																										}
																									}
																								}
																							}
																						},
																						"else": {
																							"if": {
																								"required": [
																									"league_name"
																								],
																								"properties": {
																									"league_name": {
																										"contains": {
																											"required": [
																												"value"
																											],
																											"properties": {
																												"value": {
																													"enum": [
																														"Indiska Soccer-ligan",
																														"Indian Soccer League",
																														"Indian Super League",
																														"Indyjska Soccer League",
																														"Indische Fußballliga",
																														"インド/サッカーリーグ",
																														"Liga de fútbol de la India",
																														"Liga de fútbol india",
																														"indian_soccer_league",
																														"دوري كرة القدم الهندي",
																														"Liga Indiana de Futebol",
																														"Ligue indienne de football"
																													]
																												}
																											}
																										}
																									}
																								}
																							},
																							"then": {
																								"properties": {
																									"team_name": {
																										"items": {
																											"properties": {
																												"value": {
																													"enum": [
																														"ATK 莫亨巴根足球俱乐部",
																														"东北联足球俱乐部",
																														"喀拉拉爆破手足球俱乐部",
																														"奥里萨邦足球俱乐部",
																														"孟买城足球俱乐部",
																														"果阿足球俱乐部",
																														"海得拉巴足球俱乐部",
																														"班加罗尔足球俱乐部",
																														"詹谢普尔足球俱乐部",
																														"金奈银足球俱乐部"
																													]
																												}
																											}
																										}
																									}
																								}
																							},
																							"else": {
																								"if": {
																									"required": [
																										"league_name"
																									],
																									"properties": {
																										"league_name": {
																											"contains": {
																												"required": [
																													"value"
																												],
																												"properties": {
																													"value": {
																														"enum": [
																															"Equipes nationales de football",
																															"代表チーム",
																															"National Teams",
																															"Selecciones nacionales",
																															"Landslag",
																															"Nationale teams",
																															"Squadre Nazionali",
																															"national_teams",
																															"Nationale Fußballmannschaften",
																															"Selecciones de fútbol",
																															"Seleções Nacionais",
																															"Drużyny narodowe",
																															"المنتخبات الوطنية"
																														]
																													}
																												}
																											}
																										}
																									}
																								},
																								"then": {
																									"properties": {
																										"team_name": {
																											"items": {
																												"properties": {
																													"value": {
																														"enum": [
																															"Fiji",
																															"Nauru",
																															"Tonga",
																															"Tuvalu",
																															"不丹",
																															"东帝汶",
																															"中华台北",
																															"中国",
																															"中非共和国",
																															"丹麦",
																															"乌克兰",
																															"乌兹别克斯坦",
																															"乌干达",
																															"乌拉圭",
																															"乍得",
																															"也门",
																															"亚美尼亚",
																															"以色列",
																															"伊拉克",
																															"伊朗",
																															"伯利兹",
																															"佛得角",
																															"俄罗斯",
																															"保加利亚",
																															"克罗地亚",
																															"关岛",
																															"冈比亚",
																															"冰岛",
																															"几内亚",
																															"几内亚比绍",
																															"列支敦士登",
																															"刚果",
																															"刚果民主共和国",
																															"利比亚",
																															"利比里亚",
																															"加拿大",
																															"加纳",
																															"加蓬",
																															"匈牙利",
																															"南苏丹",
																															"南非",
																															"博茨瓦纳",
																															"卡塔尔",
																															"卢旺达",
																															"卢森堡",
																															"印度尼西亚",
																															"印度音乐",
																															"危地马拉",
																															"厄瓜多尔",
																															"厄立特里亚",
																															"叙利亚",
																															"古巴",
																															"吉尔吉斯斯坦",
																															"吉布提",
																															"哈萨克斯坦",
																															"哥伦比亚",
																															"哥斯达黎加",
																															"喀麦隆",
																															"土库曼斯坦",
																															"圣卢西亚",
																															"圣基茨和尼维斯",
																															"圣多美和普林西比",
																															"圣文森特和格林纳丁斯",
																															"圣马力诺",
																															"圭亚那",
																															"坦桑尼亚",
																															"埃及",
																															"埃塞俄比亚",
																															"基里巴斯",
																															"塔吉克斯坦",
																															"塞尔维亚",
																															"塞拉利昂",
																															"塞浦路斯",
																															"塞舌尔",
																															"墨西哥",
																															"多哥",
																															"多米尼加",
																															"多米尼加共和国",
																															"奥地利",
																															"委内瑞拉",
																															"孟加拉国",
																															"安哥拉",
																															"安提瓜和巴布达",
																															"安道尔",
																															"密克罗尼西亚联邦",
																															"尼加拉瓜",
																															"尼日利亚",
																															"尼日尔",
																															"尼泊尔",
																															"巴勒斯坦",
																															"巴哈马",
																															"巴基斯坦",
																															"巴巴多斯",
																															"巴布亚新几内亚",
																															"巴拉圭",
																															"巴拿马草帽",
																															"巴林",
																															"巴西",
																															"布基纳法索",
																															"布隆迪",
																															"希腊",
																															"帕劳",
																															"库克群岛",
																															"开曼群岛",
																															"德国",
																															"意大利",
																															"所罗门群岛",
																															"拉脱维亚",
																															"挪威",
																															"捷克共和国",
																															"摩尔多瓦",
																															"摩洛哥",
																															"摩纳哥足球俱乐部",
																															"文莱",
																															"斯威士兰",
																															"斯洛伐克",
																															"斯洛文尼亚",
																															"斯里兰卡",
																															"新加坡",
																															"新西兰",
																															"日本",
																															"智利",
																															"朝鲜",
																															"柬埔寨",
																															"格林纳达",
																															"格鲁吉亚",
																															"比利时",
																															"毛里塔尼亚",
																															"毛里求斯",
																															"沙特阿拉伯",
																															"法国",
																															"波兰",
																															"波多黎各",
																															"波斯尼亚和黑塞哥维那",
																															"泰国",
																															"津巴布韦",
																															"洪都拉斯",
																															"海地",
																															"澳大利亚",
																															"火鸡肉",
																															"爱尔兰",
																															"爱沙尼亚",
																															"牙买加",
																															"特立尼达和多巴哥",
																															"独立奥林匹克运动员",
																															"玻利维亚",
																															"瑞典",
																															"瑞士",
																															"瓦努阿图",
																															"白俄罗斯",
																															"百慕大",
																															"科摩罗",
																															"科索沃",
																															"秘鲁",
																															"突尼斯",
																															"立陶宛",
																															"索马里",
																															"约旦",
																															"纳米比亚",
																															"维尔京群岛",
																															"缅甸",
																															"罗马尼亚",
																															"美国",
																															"美属萨摩亚",
																															"老挝",
																															"肯尼亚",
																															"芬兰",
																															"苏丹",
																															"苏里南",
																															"英国",
																															"英属维尔京群岛",
																															"荷兰",
																															"莫桑比克",
																															"莱索托",
																															"菲律宾",
																															"萨尔瓦多",
																															"萨摩亚",
																															"葡萄牙",
																															"蒙古",
																															"西班牙",
																															"象牙海岸",
																															"贝宁",
																															"赞比亚",
																															"赤道几内亚",
																															"越南",
																															"金翼鹦鹉",
																															"阿塞拜疆",
																															"阿富汗",
																															"阿尔及利亚",
																															"阿尔巴尼亚",
																															"阿拉伯联合酋长国",
																															"阿曼",
																															"阿根廷",
																															"阿鲁巴岛",
																															"难民奥运队",
																															"韩国",
																															"香港",
																															"马其顿",
																															"马尔代夫",
																															"马拉维",
																															"马来西亚",
																															"马绍尔群岛",
																															"马耳他",
																															"马达加斯加",
																															"马里",
																															"黎巴嫩",
																															"黑山"
																														]
																													}
																												}
																											}
																										}
																									}
																								},
																								"else": {
																									"if": {
																										"required": [
																											"league_name"
																										],
																										"properties": {
																											"league_name": {
																												"contains": {
																													"required": [
																														"value"
																													],
																													"properties": {
																														"value": {
																															"enum": [
																																"メキシコ/サッカー連盟",
																																"Mexican Football Federation",
																																"Federação Mexicana de Futebol",
																																"Mexicaanse Vereniging Van De Voetbalbond",
																																"Federazione calcistica del Messico",
																																"Federación Mexicana De Fútbol Asociación",
																																"Federación Mexicana de Fútbol Asociación",
																																"اتحاد كرة القدم المكسيكي",
																																"Fédération du Mexique de football",
																																"federacion_mexicana_de_futbol_ascn"
																															]
																														}
																													}
																												}
																											}
																										}
																									},
																									"then": {
																										"properties": {
																											"team_name": {
																												"items": {
																													"properties": {
																														"value": {
																															"enum": [
																																"atletico_de_san_luis",
																																"fc_juarez",
																																"mazatlan",
																																"necaxa",
																																"queretaro",
																																"国立自治大学俱乐部- 美洲狮",
																																"新莱昂自治大学老虎足球俱乐部",
																																"桑托斯拉古纳俱乐部",
																																"瓜达拉哈拉竞技俱乐部",
																																"美洲足球俱乐部",
																																"蒙特雷足球俱乐部",
																																"蓝十字竞技俱乐部",
																																"阿特拉斯足球俱乐部"
																															]
																														}
																													}
																												}
																											}
																										}
																									},
																									"else": {
																										"if": {
																											"required": [
																												"league_name"
																											],
																											"properties": {
																												"league_name": {
																													"contains": {
																														"required": [
																															"value"
																														],
																														"properties": {
																															"value": {
																																"enum": [
																																	"Football néerlandais",
																																	"Dutch Soccer",
																																	"Holenderska piłka nożna",
																																	"كرة القدم الهولندية",
																																	"Futebol holandês",
																																	"Fútbol holandés",
																																	"dutch_football",
																																	"Nederlands voetbal",
																																	"オランダ/サッカー",
																																	"Dutch Football",
																																	"Holländsk fotboll",
																																	"Niederländischer Fußball",
																																	"Campionato di calcio olandese"
																																]
																															}
																														}
																													}
																												}
																											}
																										},
																										"then": {
																											"properties": {
																												"team_name": {
																													"items": {
																														"properties": {
																															"value": {
																																"enum": [
																																	"ADO 海牙足球俱乐部",
																																	"almere_city",
																																	"fc_volendam",
																																	"fortuna_sittard",
																																	"NAC 布雷达足球俱乐部",
																																	"NEC 奈梅亨足球俱乐部",
																																	"PSV 埃因霍温足球俱乐部",
																																	"RBC 罗辛达足球俱乐部",
																																	"sparta_rotterdam",
																																	"乌德勒支足球俱乐部",
																																	"兹沃勒足球俱乐部",
																																	"前进之鹰足球俱乐部",
																																	"坎布尔足球俱乐部",
																																	"格罗宁根足球俱乐部",
																																	"海伦芬体育俱乐部",
																																	"特温特足球俱乐部",
																																	"瓦尔韦克足球俱乐部",
																																	"维特斯职业足球基金会",
																																	"罗达 JC 足球俱乐部",
																																	"蒂尔堡威廉二世足球俱乐部",
																																	"费耶诺德鹿特丹足球俱乐部",
																																	"赫拉克勒斯足球俱乐部",
																																	"迪加史卓普足球队",
																																	"阿尔克马尔足球俱乐部",
																																	"阿贾克斯足球俱乐部",
																																	"鹿特丹精英足球俱乐部"
																																]
																															}
																														}
																													}
																												}
																											}
																										},
																										"else": {
																											"if": {
																												"required": [
																													"league_name"
																												],
																												"properties": {
																													"league_name": {
																														"contains": {
																															"required": [
																																"value"
																															],
																															"properties": {
																																"value": {
																																	"enum": [
																																		"X-spel",
																																		"العاب اكس",
																																		"X-Games",
																																		"X Games",
																																		"Xゲーム",
																																		"x_games"
																																	]
																																}
																															}
																														}
																													}
																												}
																											},
																											"then": {
																												"properties": {
																													"team_name": {
																														"items": {
																															"properties": {
																																"value": {
																																	"enum": [
																																		"极限运动"
																																	]
																																}
																															}
																														}
																													}
																												}
																											},
																											"else": {
																												"if": {
																													"required": [
																														"league_name"
																													],
																													"properties": {
																														"league_name": {
																															"contains": {
																																"required": [
																																	"value"
																																],
																																"properties": {
																																	"value": {
																																		"enum": [
																																			"german_football",
																																			"كرة القدم الألمانية",
																																			"Tysk fotboll",
																																			"Deutscher Fußball",
																																			"Fútbol alemán",
																																			"Niemiecka piłka nożna",
																																			"Calcio tedesco",
																																			"German Soccer",
																																			"Football allemand",
																																			"Futebol alemão",
																																			"Duits voetbal",
																																			"German Football",
																																			"ドイツ/サッカー"
																																		]
																																	}
																																}
																															}
																														}
																													}
																												},
																												"then": {
																													"properties": {
																														"team_name": {
																															"items": {
																																"properties": {
																																	"value": {
																																		"enum": [
																																			"1.科隆足球俱乐部",
																																			"1_fc_magdeburg",
																																			"darmstadt_98",
																																			"fc_heidenheim",
																																			"greuther_furth",
																																			"karlsruher_sc",
																																			"union_berlin",
																																			"vfl_osnabrück",
																																			"云达不莱梅足球俱乐部",
																																			"凯泽斯劳滕足球俱乐部",
																																			"圣保利足球俱乐部",
																																			"多特蒙德足球俱乐部",
																																			"奥格斯堡足球俱乐部",
																																			"布伦瑞克足球俱乐部",
																																			"弗莱堡足球俱乐部",
																																			"德累斯顿迪纳摩足球俱乐部",
																																			"慕尼黑 1860 足球俱乐部",
																																			"拜仁慕尼黑足球俱乐部",
																																			"拜耳勒沃库森足球俱乐部",
																																			"斯图加特足球俱乐部",
																																			"杜塞尔多夫足球俱乐部",
																																			"柏林赫塔足球俱乐部",
																																			"比勒费尔德足球俱乐部",
																																			"汉堡足球俱乐部",
																																			"汉莎罗斯托克足球俱乐部",
																																			"汉诺威 96 足球俱乐部",
																																			"沃尔夫斯堡足球俱乐部",
																																			"沙尔克 04 足球俱乐部",
																																			"法兰克福足球俱乐部",
																																			"波鸿足球俱乐部",
																																			"科特布斯足球俱乐部",
																																			"纽伦堡足球俱乐部",
																																			"美因茨足球俱乐部",
																																			"莱比锡红牛足球俱乐部",
																																			"门兴格拉德巴赫足球俱乐部",
																																			"霍芬海姆足球俱乐部"
																																		]
																																	}
																																}
																															}
																														}
																													}
																												},
																												"else": {
																													"if": {
																														"required": [
																															"league_name"
																														],
																														"properties": {
																															"league_name": {
																																"contains": {
																																	"required": [
																																		"value"
																																	],
																																	"properties": {
																																		"value": {
																																			"enum": [
																																				"الدوري الوطني لكرة القدم للسيدات",
																																				"NWSL",
																																				"nwsl"
																																			]
																																		}
																																	}
																																}
																															}
																														}
																													},
																													"then": {
																														"properties": {
																															"team_name": {
																																"items": {
																																	"properties": {
																																		"value": {
																																			"enum": [
																																				"休斯顿达斯足球俱乐部",
																																				"北卡罗来纳勇气队",
																																				"华盛顿精神足球俱乐部",
																																				"圣地亚哥波浪足球俱乐部",
																																				"堪萨斯城潮流",
																																				"天使城足球俱乐部",
																																				"奥兰多荣耀足球队",
																																				"帝王足球俱乐部",
																																				"新泽西/纽约哥谭足球队",
																																				"新泽西州/纽约哥谭足球俱乐部",
																																				"波特兰荆棘队",
																																				"犹他皇家足球队",
																																				"芝加哥红星队",
																																				"路易斯维尔赛车队"
																																			]
																																		}
																																	}
																																}
																															}
																														}
																													},
																													"else": {
																														"if": {
																															"required": [
																																"league_name"
																															],
																															"properties": {
																																"league_name": {
																																	"contains": {
																																		"required": [
																																			"value"
																																		],
																																		"properties": {
																																			"value": {
																																				"enum": [
																																					"Pro Kabaddi League",
																																					"pro_kabaddi_league",
																																					"Pro Kabaddi Ligan",
																																					"دوري كبادي للمحترفين",
																																					"Liga Pro Kabaddi",
																																					"インド/プロカバディリーグ"
																																				]
																																			}
																																		}
																																	}
																																}
																															}
																														},
																														"then": {
																															"properties": {
																																"team_name": {
																																	"items": {
																																		"properties": {
																																			"value": {
																																				"enum": [
																																					"乌姆巴队",
																																					"古吉拉特邦巨星队",
																																					"哈里亚纳钢人队",
																																					"大邦德里队",
																																					"孟加拉勇士队",
																																					"尤达队",
																																					"巴特那海盗队",
																																					"斋浦尔粉红豹队",
																																					"普内里帕坦队",
																																					"泰卢固泰坦队",
																																					"泰米尔塔莱瓦斯队",
																																					"班加罗尔公牛队"
																																				]
																																			}
																																		}
																																	}
																																}
																															}
																														},
																														"else": {
																															"if": {
																																"required": [
																																	"league_name"
																																],
																																"properties": {
																																	"league_name": {
																																		"contains": {
																																			"required": [
																																				"value"
																																			],
																																			"properties": {
																																				"value": {
																																					"enum": [
																																						"رابطة لاعبي الغولف المحترفين",
																																						"pga",
																																						"PGA",
																																						"PGAツアー"
																																					]
																																				}
																																			}
																																		}
																																	}
																																}
																															},
																															"then": {
																																"properties": {
																																	"team_name": {
																																		"items": {
																																			"properties": {
																																				"value": {
																																					"enum": [
																																						"PGA"
																																					]
																																				}
																																			}
																																		}
																																	}
																																}
																															},
																															"else": {
																																"if": {
																																	"required": [
																																		"league_name"
																																	],
																																	"properties": {
																																		"league_name": {
																																			"contains": {
																																				"required": [
																																					"value"
																																				],
																																				"properties": {
																																					"value": {
																																						"enum": [
																																							"Schottischer Fußball",
																																							"كرة القدم الاسكتلندية",
																																							"Skotsk fotboll",
																																							"Futebol escocês",
																																							"Football écossais",
																																							"Fútbol escocés",
																																							"Scottish Soccer",
																																							"Scottish Football",
																																							"Schots voetbal",
																																							"スコットランド/サッカー",
																																							"Szkocka piłka nożna",
																																							"scottish_football",
																																							"Campionato di calcio scozzese"
																																						]
																																					}
																																				}
																																			}
																																		}
																																	}
																																},
																																"then": {
																																	"properties": {
																																		"team_name": {
																																			"items": {
																																				"properties": {
																																					"value": {
																																						"enum": [
																																							"airdrieonians",
																																							"arbroath",
																																							"inverness_caledonian_thistle",
																																							"ross_county",
																																							"st_johnstone",
																																							"st_mirren",
																																							"凯尔特人足球俱乐部",
																																							"利文斯顿足球俱乐部",
																																							"基尔马诺克足球俱乐部",
																																							"希伯尼安足球俱乐部",
																																							"帕尔蒂克足球俱乐部",
																																							"心脏",
																																							"流浪者足球俱乐部",
																																							"邓弗姆林竞技足球俱乐部",
																																							"邓迪联足球俱乐部",
																																							"邓迪足球俱乐部",
																																							"阿伯丁足球俱乐部",
																																							"马瑟韦尔足球俱乐部"
																																						]
																																					}
																																				}
																																			}
																																		}
																																	}
																																},
																																"else": {
																																	"if": {
																																		"required": [
																																			"league_name"
																																		],
																																		"properties": {
																																			"league_name": {
																																				"contains": {
																																					"required": [
																																						"value"
																																					],
																																					"properties": {
																																						"value": {
																																							"enum": [
																																								"Französischer Fußball",
																																								"Frans voetbal",
																																								"French Football",
																																								"Fransk fotboll",
																																								"French Soccer",
																																								"Francuska piłka nożna",
																																								"Calcio francese",
																																								"Futebol francês",
																																								"french_football",
																																								"كرة القدم الفرنسية",
																																								"Fútbol francés",
																																								"フランス/サッカー",
																																								"Football français"
																																							]
																																						}
																																					}
																																				}
																																			}
																																		}
																																	},
																																	"then": {
																																		"properties": {
																																			"team_name": {
																																				"items": {
																																					"properties": {
																																						"value": {
																																							"enum": [
																																								"clermont_foot_63",
																																								"PSG",
																																								"亚眠竞技俱乐部",
																																								"依云足球俱乐部",
																																								"兰斯足球俱乐部",
																																								"勒阿弗尔足球俱乐部",
																																								"南特足球俱乐部",
																																								"卡昂足球俱乐部",
																																								"图卢兹足球俱乐部",
																																								"圣埃蒂安足球俱乐部",
																																								"小轿车",
																																								"尼姆奥林匹克足球俱乐部",
																																								"尼斯足球俱乐部",
																																								"巴斯蒂亚足球俱乐部",
																																								"布雷斯特足球俱乐部",
																																								"摩纳哥足球俱乐部",
																																								"斯特拉斯堡足球俱乐部",
																																								"昂热足球俱乐部",
																																								"梅斯足球俱乐部",
																																								"欧塞尔足球俱乐部",
																																								"波尔多瓶",
																																								"洛里昂足球俱乐部",
																																								"特鲁瓦足球俱乐部",
																																								"瓦朗谢讷足球俱乐部",
																																								"甘冈足球俱乐部",
																																								"第戎足球俱乐部",
																																								"索肖足球俱乐部",
																																								"蒙彼利埃足球俱乐部",
																																								"里尔足球俱乐部",
																																								"里昂足球俱乐部",
																																								"镜片",
																																								"阿雅克肖足球俱乐部",
																																								"雷恩足球俱乐部",
																																								"马赛足球俱乐部"
																																							]
																																						}
																																					}
																																				}
																																			}
																																		}
																																	},
																																	"else": {
																																		"if": {
																																			"required": [
																																				"league_name"
																																			],
																																			"properties": {
																																				"league_name": {
																																					"contains": {
																																						"required": [
																																							"value"
																																						],
																																						"properties": {
																																							"value": {
																																								"enum": [
																																									"LPGAツアー",
																																									"lpga",
																																									"رابطة لاعبات الغولف المحترفات",
																																									"LPGA"
																																								]
																																							}
																																						}
																																					}
																																				}
																																			}
																																		},
																																		"then": {
																																			"properties": {
																																				"team_name": {
																																					"items": {
																																						"properties": {
																																							"value": {
																																								"enum": [
																																									"LPGA"
																																								]
																																							}
																																						}
																																					}
																																				}
																																			}
																																		},
																																		"else": {
																																			"if": {
																																				"required": [
																																					"league_name"
																																				],
																																				"properties": {
																																					"league_name": {
																																						"contains": {
																																							"required": [
																																								"value"
																																							],
																																							"properties": {
																																								"value": {
																																									"enum": [
																																										"لعبة الكريكيت الدولية",
																																										"Międzynarodowy krykiet",
																																										"Internationales Cricket",
																																										"クリケット代表チーム",
																																										"international_cricket",
																																										"Campionato internazionale di cricket",
																																										"Internationell Cricket",
																																										"Cricket international",
																																										"Cricket internacional",
																																										"Críquete Internacional",
																																										"International Cricket"
																																									]
																																								}
																																							}
																																						}
																																					}
																																				}
																																			},
																																			"then": {
																																				"properties": {
																																					"team_name": {
																																						"items": {
																																							"properties": {
																																								"value": {
																																									"enum": [
																																										"南非",
																																										"印度音乐",
																																										"孟加拉国",
																																										"巴基斯坦",
																																										"斯里兰卡",
																																										"新西兰",
																																										"津巴布韦",
																																										"澳大利亚",
																																										"爱尔兰",
																																										"英国",
																																										"西印度群岛",
																																										"阿富汗"
																																									]
																																								}
																																							}
																																						}
																																					}
																																				}
																																			},
																																			"else": {
																																				"if": {
																																					"required": [
																																						"league_name"
																																					],
																																					"properties": {
																																						"league_name": {
																																							"contains": {
																																								"required": [
																																									"value"
																																								],
																																								"properties": {
																																									"value": {
																																										"enum": [
																																											"Nationella Lacrosse-ligan",
																																											"دوري لاكروس الوطني",
																																											"ナショナル・ラクロス・リーグ",
																																											"National Lacrosse League",
																																											"Liga Nacional de Lacrosse",
																																											"Liga nacional de lacrosse",
																																											"national_lacrosse_league",
																																											"Narodowa Liga Lacrosse"
																																										]
																																									}
																																								}
																																							}
																																						}
																																					}
																																				},
																																				"then": {
																																					"properties": {
																																						"team_name": {
																																							"items": {
																																								"properties": {
																																									"value": {
																																										"enum": [
																																											"乔治亚蜂群队",
																																											"卡尔加里罗内克斯队",
																																											"哈利法克斯雷鸟队",
																																											"圣地亚哥海豹队",
																																											"多伦多摇滚队",
																																											"新英格兰黑狼队",
																																											"水牛城强盗队",
																																											"温哥华勇士队",
																																											"科罗拉多猛犸队",
																																											"纽约激流队",
																																											"罗切斯特骑士鹰队",
																																											"萨斯喀彻温热潮队",
																																											"费城之翼队"
																																										]
																																									}
																																								}
																																							}
																																						}
																																					}
																																				},
																																				"else": {
																																					"if": {
																																						"required": [
																																							"league_name"
																																						],
																																						"properties": {
																																							"league_name": {
																																								"contains": {
																																									"required": [
																																										"value"
																																									],
																																									"properties": {
																																										"value": {
																																											"enum": [
																																												"hockey_india_league",
																																												"Liga De Hóquei Indiano",
																																												"Hockey India League",
																																												"Hokejowa liga indyjska",
																																												"Liga India de Hockey",
																																												"ホッケーインドリーグ",
																																												"Liga india de hockey",
																																												"Indiens hockeyliga",
																																												"دوري الهوكي الهندي",
																																												"Campionato di hockey indiano"
																																											]
																																										}
																																									}
																																								}
																																							}
																																						}
																																					},
																																					"then": {
																																						"properties": {
																																							"team_name": {
																																								"items": {
																																									"properties": {
																																										"value": {
																																											"enum": [
																																												"兰契雷曲棍球队",
																																												"北方邦奇才队",
																																												"卡林加枪骑兵队",
																																												"大邦孟买 HC 队",
																																												"德里乘波者队",
																																												"杰皮旁遮普勇士队"
																																											]
																																										}
																																									}
																																								}
																																							}
																																						}
																																					},
																																					"else": {
																																						"if": {
																																							"required": [
																																								"league_name"
																																							],
																																							"properties": {
																																								"league_name": {
																																									"contains": {
																																										"required": [
																																											"value"
																																										],
																																										"properties": {
																																											"value": {
																																												"enum": [
																																													"كرة القدم الايطالية",
																																													"italian_football",
																																													"Italienischer Fußball",
																																													"Italian Soccer",
																																													"Włoska piłka nożna",
																																													"Italiensk fotboll",
																																													"Football italien",
																																													"Calcio italiano",
																																													"Italiaans voetbal",
																																													"Fútbol italiano",
																																													"Futebol italiano",
																																													"Italian Football",
																																													"イタリア/サッカー"
																																												]
																																											}
																																										}
																																									}
																																								}
																																							}
																																						},
																																						"then": {
																																							"properties": {
																																								"team_name": {
																																									"items": {
																																										"properties": {
																																											"value": {
																																												"enum": [
																																													"A.S.利沃诺足球俱乐部",
																																													"AC 米兰足球俱乐部",
																																													"Roma",
																																													"乌迪内斯足球俱乐部",
																																													"亚特兰大足球俱乐部",
																																													"佛罗伦萨足球俱乐部",
																																													"佩鲁贾足球俱乐部",
																																													"切沃维罗纳足球俱乐部",
																																													"博洛尼亚足球俱乐部",
																																													"卡利亚里足球俱乐部",
																																													"卡坦扎罗",
																																													"卡塔尼亚足球俱乐部",
																																													"国际米兰",
																																													"奇塔代拉",
																																													"威尼斯足球俱乐部",
																																													"尤文图斯足球俱乐部",
																																													"巴勒莫",
																																													"巴里",
																																													"布雷西亚足球俱乐部",
																																													"帕尔马足球俱乐部",
																																													"弗罗西诺内足球俱乐部",
																																													"恩波利足球俱乐部",
																																													"拉齐奥",
																																													"摩德纳足球俱乐部",
																																													"斯帕尔足球俱乐部",
																																													"桑普多利亚足球俱乐部",
																																													"比萨",
																																													"泰尔纳纳",
																																													"热那亚足球俱乐部",
																																													"皮亚琴察足球俱乐部",
																																													"科森察",
																																													"科莫足球俱乐部",
																																													"维罗纳足球俱乐部",
																																													"莱切",
																																													"莱科",
																																													"萨勒尼塔纳足球俱乐部",
																																													"萨索罗足球俱乐部",
																																													"蒙扎",
																																													"那不勒斯足球俱乐部",
																																													"都灵",
																																													"阿斯科利",
																																													"雷吉纳足球俱乐部"
																																												]
																																											}
																																										}
																																									}
																																								}
																																							}
																																						},
																																						"else": {
																																							"if": {
																																								"required": [
																																									"league_name"
																																								],
																																								"properties": {
																																									"league_name": {
																																										"contains": {
																																											"required": [
																																												"value"
																																											],
																																											"properties": {
																																												"value": {
																																													"enum": [
																																														"Campionato di pallavolo professionale",
																																														"دوري المحترفين للكرة الطائرة",
																																														"インド/バレーボールリーグ",
																																														"Pro Volleyball Ligan",
																																														"Pro Volleyball League",
																																														"pro_volleyball_league",
																																														"Liga Profissional de Vôlei",
																																														"Liga de voleibol profesional"
																																													]
																																												}
																																											}
																																										}
																																									}
																																								}
																																							},
																																							"then": {
																																								"properties": {
																																									"team_name": {
																																										"items": {
																																											"properties": {
																																												"value": {
																																													"enum": [
																																														"乌姆巴排球对",
																																														"卡利卡特英雄队",
																																														"艾哈迈达巴德后卫队",
																																														"钦奈斯巴达人队",
																																														"高知蓝强攻队",
																																														"黑鹰海得拉巴队"
																																													]
																																												}
																																											}
																																										}
																																									}
																																								}
																																							},
																																							"else": {
																																								"if": {
																																									"required": [
																																										"league_name"
																																									],
																																									"properties": {
																																										"league_name": {
																																											"contains": {
																																												"required": [
																																													"value"
																																												],
																																												"properties": {
																																													"value": {
																																														"enum": [
																																															"Japońska piłka nożna",
																																															"Campionato di calcio giapponese",
																																															"Japansk fotboll",
																																															"Japanese Soccer",
																																															"Japanischer Fußball",
																																															"Japans voetbal",
																																															"Japanese Football",
																																															"Futebol japonês",
																																															"كرة القدم اليابانية",
																																															"日本/Jリーグ",
																																															"Fútbol japonés",
																																															"japanese_football",
																																															"Football japonais"
																																														]
																																													}
																																												}
																																											}
																																										}
																																									}
																																								},
																																								"then": {
																																									"properties": {
																																										"team_name": {
																																											"items": {
																																												"properties": {
																																													"value": {
																																														"enum": [
																																															"F.C.岐阜足球俱乐部",
																																															"东京绿茵足球俱乐部",
																																															"京都不死鸟足球俱乐部",
																																															"仙台七夕足球队",
																																															"冈山绿雉足球俱乐部",
																																															"北九州向日葵队",
																																															"名古屋鲸鱼队",
																																															"大分三神足球队",
																																															"大宫松鼠足球俱乐部",
																																															"大阪樱花队",
																																															"大阪钢巴足球俱乐部",
																																															"富山胜利足球队",
																																															"山口雷法足球俱乐部",
																																															"山形山神足球俱乐部",
																																															"岩手盛冈仙鹤足球俱乐部",
																																															"川崎前锋队",
																																															"市原千叶足球俱乐部",
																																															"广岛三箭足球队",
																																															"德岛漩涡足球俱乐部",
																																															"新潟天鹅队",
																																															"札幌冈萨多足球队",
																																															"松本山雅足球俱乐部",
																																															"柏太阳神足球队",
																																															"栃木足球俱乐部",
																																															"横滨体育及文化会",
																																															"横滨水手队",
																																															"横滨足球俱乐部",
																																															"水户蜀葵足球队",
																																															"浦和红钻足球俱乐部",
																																															"清水心跳足球队",
																																															"湘南丽海足球队",
																																															"熊本深红足球俱乐部",
																																															"爱媛足球俱乐部",
																																															"琉球足球俱乐部",
																																															"甲府风林足球俱乐部",
																																															"町田泽维亚足球俱乐部",
																																															"相模原体育俱乐部",
																																															"磐城足球俱乐部",
																																															"磐田喜悦足球队",
																																															"神户胜利船队",
																																															"福冈黄蜂足球俱乐部",
																																															"福岛联足球俱乐部",
																																															"秋田蓝闪电足球俱乐部",
																																															"群马草津温泉队",
																																															"藤枝 MYFC 足球俱乐部",
																																															"赞岐釜玉海足球队",
																																															"足球俱乐部东京",
																																															"金泽塞维根足球俱乐部",
																																															"长崎成功丸足球俱乐部",
																																															"长野帕塞罗体育俱乐部",
																																															"鸟取飞翔足球俱乐部",
																																															"鸟栖砂岩足球队",
																																															"鹿岛鹿角足球俱乐部"
																																														]
																																													}
																																												}
																																											}
																																										}
																																									}
																																								},
																																								"else": {
																																									"if": {
																																										"required": [
																																											"league_name"
																																										],
																																										"properties": {
																																											"league_name": {
																																												"contains": {
																																													"required": [
																																														"value"
																																													],
																																													"properties": {
																																														"value": {
																																															"enum": [
																																																"البيسبول الكلاسيكية العالمية",
																																																"world_baseball_classic",
																																																"Clásico mundial de béisbol",
																																																"Classique mondiale de baseball",
																																																"Wereld Honkbal Klassieker",
																																																"World Baseball Classic",
																																																"Clássico Mundial de Beisebol",
																																																"ワールドベースボールクラシック"
																																															]
																																														}
																																													}
																																												}
																																											}
																																										}
																																									},
																																									"then": {
																																										"properties": {
																																											"team_name": {
																																												"items": {
																																													"properties": {
																																														"value": {
																																															"enum": [
																																																"古巴队",
																																																"多米尼加共和国队",
																																																"委内瑞拉队",
																																																"巴拿马队",
																																																"日本队",
																																																"波多黎各队",
																																																"美国队",
																																																"荷兰队",
																																																"韩国队"
																																															]
																																														}
																																													}
																																												}
																																											}
																																										}
																																									},
																																									"else": {
																																										"if": {
																																											"required": [
																																												"league_name"
																																											],
																																											"properties": {
																																												"league_name": {
																																													"contains": {
																																														"required": [
																																															"value"
																																														],
																																														"properties": {
																																															"value": {
																																																"enum": [
																																																	"Indiska Premier-ligan",
																																																	"Szkocka Premier League",
																																																	"Liga Premier de la India",
																																																	"Indian Premier League",
																																																	"インディアン・プレミアリーグ",
																																																	"الدوري الهندي الممتاز",
																																																	"Liga Premier Indiana",
																																																	"indian_premier_league",
																																																	"Premier League india"
																																																]
																																															}
																																														}
																																													}
																																												}
																																											}
																																										},
																																										"then": {
																																											"properties": {
																																												"team_name": {
																																													"items": {
																																														"properties": {
																																															"value": {
																																																"enum": [
																																																	"加尔各答骑士团队",
																																																	"古吉拉特狮队",
																																																	"国王十一旁遮普队",
																																																	"孟买印度人队",
																																																	"德里首都队",
																																																	"拉贾斯坦皇家队",
																																																	"朝阳海德拉巴队",
																																																	"班加罗尔皇家挑战者俱乐部",
																																																	"进击的浦那超级巨人队",
																																																	"钦奈超级国王队"
																																																]
																																															}
																																														}
																																													}
																																												}
																																											}
																																										},
																																										"else": {
																																											"if": {
																																												"required": [
																																													"league_name"
																																												],
																																												"properties": {
																																													"league_name": {
																																														"contains": {
																																															"required": [
																																																"value"
																																															],
																																															"properties": {
																																																"value": {
																																																	"enum": [
																																																		"wwe",
																																																		"WWE",
																																																		"المصارعة العالمية الترفيهية"
																																																	]
																																																}
																																															}
																																														}
																																													}
																																												}
																																											},
																																											"then": {
																																												"properties": {
																																													"team_name": {
																																														"items": {
																																															"properties": {
																																																"value": {
																																																	"enum": [
																																																		"WWE"
																																																	]
																																																}
																																															}
																																														}
																																													}
																																												}
																																											},
																																											"else": {
																																												"if": {
																																													"required": [
																																														"league_name"
																																													],
																																													"properties": {
																																														"league_name": {
																																															"contains": {
																																																"required": [
																																																	"value"
																																																],
																																																"properties": {
																																																	"value": {
																																																		"enum": [
																																																			"اتحاد كرة القدم الأميركي",
																																																			"nfl",
																																																			"NFL"
																																																		]
																																																	}
																																																}
																																															}
																																														}
																																													}
																																												},
																																												"then": {
																																													"properties": {
																																														"team_name": {
																																															"items": {
																																																"properties": {
																																																	"value": {
																																																		"enum": [
																																																			"丹佛野马队",
																																																			"亚利桑那红雀队",
																																																			"亚特兰大猎鹰队",
																																																			"休斯顿德州人队",
																																																			"克利夫兰布朗队",
																																																			"匹兹堡钢人队",
																																																			"华盛顿指挥官",
																																																			"华盛顿美式足球队",
																																																			"卡罗莱纳黑豹队",
																																																			"印第安纳波利斯小马队",
																																																			"坦帕湾海盗队",
																																																			"堪萨斯城酋长队",
																																																			"奥克兰突袭者队",
																																																			"巴尔的摩乌鸦队",
																																																			"布法罗比尔队",
																																																			"底特律雄狮队",
																																																			"拉斯维加斯突袭者队",
																																																			"新奥尔良圣徒队",
																																																			"新英格兰爱国者队",
																																																			"旧金山49人队",
																																																			"明尼苏达维京人队",
																																																			"杰克逊维尔美洲虎队",
																																																			"洛杉矶充电器",
																																																			"洛杉矶公羊队",
																																																			"洛杉矶闪电",
																																																			"田纳西巨神队",
																																																			"田纳西泰坦队",
																																																			"纽约喷气机队",
																																																			"纽约巨人队",
																																																			"绿湾包装工队",
																																																			"芝加哥熊队",
																																																			"西雅图海鹰队",
																																																			"费城老鹰队",
																																																			"辛辛那提猛虎队",
																																																			"达拉斯德州人队",
																																																			"达拉斯牛仔队",
																																																			"迈阿密海豚队"
																																																		]
																																																	}
																																																}
																																															}
																																														}
																																													}
																																												},
																																												"else": {
																																													"if": {
																																														"required": [
																																															"league_name"
																																														],
																																														"properties": {
																																															"league_name": {
																																																"contains": {
																																																	"required": [
																																																		"value"
																																																	],
																																																	"properties": {
																																																		"value": {
																																																			"enum": [
																																																				"ufc",
																																																				"UFC",
																																																				"بطولة القتال النهائي"
																																																			]
																																																		}
																																																	}
																																																}
																																															}
																																														}
																																													},
																																													"then": {
																																														"properties": {
																																															"team_name": {
																																																"items": {
																																																	"properties": {
																																																		"value": {
																																																			"enum": [
																																																				"TJ·迪拉肖",
																																																				"中量级",
																																																				"丹·胡克",
																																																				"丹尼尔·科米尔",
																																																				"丽兹·卡穆什",
																																																				"丽娜·兰斯伯格",
																																																				"乌利亚·霍尔",
																																																				"乔什·埃米特",
																																																				"乔安妮·考尔德伍德",
																																																				"乔安娜·耶德尔泽奇克",
																																																				"乔恩·琼斯",
																																																				"乔治·圣皮埃尔",
																																																				"亚历克斯·佩雷斯",
																																																				"亚历克斯·奥利维拉",
																																																				"亚历克萨·格拉索",
																																																				"亚历克西斯·戴维斯",
																																																				"亚历山大·古斯塔夫森",
																																																				"亚历山大·埃尔南德斯",
																																																				"亚历山大·沃尔卡诺斯基",
																																																				"亚历山大·沃尔科夫",
																																																				"亚历山大·潘托哈",
																																																				"亚历杭德罗·佩雷斯",
																																																				"亚娜·库尼茨卡娅",
																																																				"亚尔·罗德里格斯",
																																																				"亨利·塞胡多",
																																																				"伊利尔·拉提菲",
																																																				"伊斯雷尔·阿德桑亚",
																																																				"佐佐木佑太",
																																																				"何塞·奥尔多",
																																																				"佩德罗·蒙霍兹",
																																																				"保罗·科斯塔",
																																																				"保罗·费尔德",
																																																				"克劳迪娅·加德哈",
																																																				"克里斯·赛博格",
																																																				"克里斯·魏德曼",
																																																				"兰达·马科斯",
																																																				"内特·迪亚兹",
																																																				"凯伦·维埃拉",
																																																				"凯文·李",
																																																				"凯文·盖斯特鲁姆",
																																																				"凯特·辛加诺",
																																																				"凯特琳·乔卡吉安",
																																																				"劳伦·墨菲增加 1",
																																																				"卡哈比·努尔马格妙多夫",
																																																				"卡布·斯旺森",
																																																				"卡拉·埃斯帕萨",
																																																				"卡罗琳娜·科沃凯维奇",
																																																				"卡马鲁·乌斯曼",
																																																				"卢克·洛克霍尔德",
																																																				"吉米·里维拉",
																																																				"吉米·马努瓦",
																																																				"唐纳德·塞罗内",
																																																				"圣地亚哥·庞齐尼比奥",
																																																				"埃利亚斯·西奥多鲁",
																																																				"埃利泽·扎莱斯基·多斯桑托斯",
																																																				"埃德森·巴博萨",
																																																				"塔蒂亚娜·苏亚雷斯",
																																																				"塞尔吉奥·佩蒂斯",
																																																				"墨里西欧·鲁阿",
																																																				"多米尼克·克鲁兹",
																																																				"多米尼克·雷耶斯",
																																																				"大卫·布兰奇",
																																																				"奥列克西·奥利尼克",
																																																				"奥文斯·圣普勒",
																																																				"威尔逊·雷斯",
																																																				"安东尼·佩蒂斯",
																																																				"安东尼·史密斯",
																																																				"安吉拉·希尔",
																																																				"安德烈·阿尔洛夫斯基",
																																																				"安德里亚·李",
																																																				"小安东尼奥·卡洛斯",
																																																				"尤西尔·福米加",
																																																				"尼可·蒙塔尼奥",
																																																				"尼基塔·克雷洛夫",
																																																				"尼娜·安萨罗夫",
																																																				"尼尔·马格尼",
																																																				"布兰登·莫雷诺",
																																																				"布拉德·塔瓦雷斯",
																																																				"布赖恩·奥尔特加",
																																																				"康纳·麦格雷戈",
																																																				"弗兰基·埃德加",
																																																				"弗朗西斯·纳干诺",
																																																				"弗朗西斯科·特里纳尔多",
																																																				"德米安·迈亚",
																																																				"德维森·菲格雷多",
																																																				"德里克·刘易斯",
																																																				"德里克·布伦森",
																																																				"扎比特·马戈梅德沙里波夫",
																																																				"托尼·弗格森",
																																																				"托尼亚·埃文格",
																																																				"托马斯·阿尔梅达",
																																																				"扬·布拉霍维奇",
																																																				"拉奎尔·彭宁顿",
																																																				"拉尼·叶海亚",
																																																				"拉斐尔·多斯·安霍斯",
																																																				"拉斐尔·阿松桑",
																																																				"斯特凡·斯特鲁夫",
																																																				"斯蒂普·米欧奇",
																																																				"斯蒂芬·汤普森",
																																																				"本·阮",
																																																				"朱尼尔·多斯·桑托斯",
																																																				"朱莉安娜·佩纳",
																																																				"杰曼·德·兰达米",
																																																				"杰西卡·埃",
																																																				"杰西卡·安德拉德",
																																																				"杰西卡·罗斯·克拉克",
																																																				"杰里米·斯蒂芬斯",
																																																				"查德·门德斯",
																																																				"柯蒂斯·布莱兹",
																																																				"格洛弗·特谢拉",
																																																				"沃尔坎·厄兹德米尔",
																																																				"沙米尔·阿卜杜拉希莫夫",
																																																				"泰·特瓦萨",
																																																				"泰伦·伍德利",
																																																				"泰森·佩德罗",
																																																				"特西亚·托雷斯",
																																																				"玛丽昂·雷诺",
																																																				"玛拉·罗梅罗·博雷拉",
																																																				"瓦伦缇娜·舍甫琴科",
																																																				"甘纳·纳尔逊",
																																																				"科尔比·科文顿",
																																																				"科特尼·凯西",
																																																				"科迪·加布兰特",
																																																				"科迪·斯塔曼",
																																																				"科里·安德森",
																																																				"米尔萨德·贝克蒂奇",
																																																				"米歇尔·沃特森",
																																																				"米沙·齐尔库诺夫",
																																																				"约尔·罗梅罗",
																																																				"约瑟夫·贝纳维德兹",
																																																				"约翰·多德森",
																																																				"约翰·莫拉加",
																																																				"约翰·莱因克尔",
																																																				"罗伯特·惠特克",
																																																				"罗克珊·莫达菲里",
																																																				"罗布·冯特",
																																																				"罗斯·娜玛朱纳斯",
																																																				"罗比·劳勒",
																																																				"艾琳·阿尔达娜",
																																																				"菲利斯·赫里格",
																																																				"萨拉·麦克曼",
																																																				"蒂亚戈·桑托斯",
																																																				"蒂姆·埃利奥特",
																																																				"西贾拉·尤班克斯",
																																																				"詹妮弗·迈亚",
																																																				"詹姆斯·维克",
																																																				"豪尔赫·马斯维达尔",
																																																				"贝瑟·科雷娅",
																																																				"贾卡雷·苏扎",
																																																				"贾斯汀·加瑟基",
																																																				"贾斯汀·威利斯",
																																																				"贾里德·坎诺尼尔",
																																																				"达伦·埃尔金斯",
																																																				"达伦·蒂尔",
																																																				"达斯汀·奥尔蒂斯",
																																																				"达斯汀·波里耶",
																																																				"迈克尔·基耶萨",
																																																				"道格拉斯·席尔瓦·德·安德拉德",
																																																				"郑赞盛",
																																																				"里卡多·拉马斯",
																																																				"里昂·爱德华兹",
																																																				"阿什莉·埃文斯-史密斯",
																																																				"阿利斯泰尔·奥弗瑞姆",
																																																				"阿尔·亚昆塔",
																																																				"阿斯彭·莱德",
																																																				"阿曼达·努内斯",
																																																				"阿贾曼·斯特林",
																																																				"雷·博格",
																																																				"雷纳托·莫伊卡诺",
																																																				"霍莉·霍尔姆",
																																																				"露西·普迪洛娃",
																																																				"马修斯·尼科劳",
																																																				"马克·亨特",
																																																				"马克斯·霍洛威",
																																																				"马辛·泰布拉",
																																																				"马龙·莫拉斯",
																																																				"麦肯齐·邓恩"
																																																			]
																																																		}
																																																	}
																																																}
																																															}
																																														}
																																													},
																																													"else": {
																																														"if": {
																																															"required": [
																																																"league_name"
																																															],
																																															"properties": {
																																																"league_name": {
																																																	"contains": {
																																																		"required": [
																																																			"value"
																																																		],
																																																		"properties": {
																																																			"value": {
																																																				"enum": [
																																																					"Baloncesto japonés",
																																																					"Basketball japonais",
																																																					"Japanese Basketball",
																																																					"日本/Bリーグ",
																																																					"japanese_basketball",
																																																					"Japansk basket",
																																																					"Japans basketbal",
																																																					"Basquete japonês",
																																																					"كرة السلة اليابانية",
																																																					"Japońska koszykówka",
																																																					"Campionato di basket giapponese",
																																																					"Japanischer Basketball"
																																																				]
																																																			}
																																																		}
																																																	}
																																																}
																																															}
																																														},
																																														"then": {
																																															"properties": {
																																																"team_name": {
																																																	"items": {
																																																		"properties": {
																																																			"value": {
																																																				"enum": [
																																																					"AISIN AW AREIONS ANJO",
																																																					"AKITA NORTHERN HAPPINETS",
																																																					"ALVARK TOKYO",
																																																					"AOMORI WAT'S",
																																																					"BAMBITIOUS NARA",
																																																					"CHIBA JETS",
																																																					"EARTHFRIENDS TOKYO Z",
																																																					"EHIME ORANGE VIKINGS",
																																																					"F EAGLES NAGOYA",
																																																					"FUKUSHIMA FIREBONDS",
																																																					"GUNMA CRANE THUNDERS",
																																																					"HIROSHIMA DRAGONFLIES",
																																																					"IBARAKI ROBOTS",
																																																					"IWATE BIG BULLS",
																																																					"KAGAWA FIVE ARROWS",
																																																					"KAGOSHIMA REBNISE",
																																																					"KANAZAWA SAMURAIZ",
																																																					"KAWASAKI BRAVE THUNDERS",
																																																					"KUMAMOTO VOLTERS",
																																																					"KYOTO HANNARYZ",
																																																					"LEVANGA HOKKAIDO",
																																																					"NAGOYA DIAMOND DOLPHINS",
																																																					"NIIGATA ALBIREX BB",
																																																					"NISHINOMIYA STORKS",
																																																					"OSAKA EVESSA",
																																																					"OTSUKA CORPORATION ALPHAS",
																																																					"RIZING ZEPHYR FUKUOKA",
																																																					"RYUKYU GOLDEN KINGS",
																																																					"SAITAMA BRONCOS",
																																																					"SAN-EN NEOPHOENIX",
																																																					"SEAHORSES MIKAWA",
																																																					"SENDAI EIGHTY NINERS",
																																																					"SHIGA LAKESTARS",
																																																					"SHIMANE SUSANOO MAGIC",
																																																					"SHINSHU BRAVE WARRIORS",
																																																					"SUNROCKERS SHIBUYA",
																																																					"TOCHIGI BREX",
																																																					"TOKIO MARINE NICHIDO BIG BLUE",
																																																					"TOKYO CINQ REVES",
																																																					"TOKYO EXCELLENCE",
																																																					"TOKYO HACHIOJI TRAINS",
																																																					"TOYAMA GROUSES",
																																																					"TOYODA GOSEI SCORPIONS",
																																																					"YAMAGATA WYVERNS",
																																																					"YOKOHAMA B-CORSAIRS"
																																																				]
																																																			}
																																																		}
																																																	}
																																																}
																																															}
																																														},
																																														"else": {
																																															"if": {
																																																"required": [
																																																	"league_name"
																																																],
																																																"properties": {
																																																	"league_name": {
																																																		"contains": {
																																																			"required": [
																																																				"value"
																																																			],
																																																			"properties": {
																																																				"value": {
																																																					"enum": [
																																																						"wnba",
																																																						"الاتحاد الوطني لكرة السلة النسائية",
																																																						"WNBA"
																																																					]
																																																				}
																																																			}
																																																		}
																																																	}
																																																}
																																															},
																																															"then": {
																																																"properties": {
																																																	"team_name": {
																																																		"items": {
																																																			"properties": {
																																																				"value": {
																																																					"enum": [
																																																						"亚特兰大梦想队",
																																																						"休斯顿彗星队",
																																																						"克里夫兰摇滚者队",
																																																						"华盛顿神秘人队",
																																																						"印第安纳狂热队",
																																																						"圣安东尼奥银星队",
																																																						"塔尔萨震动队",
																																																						"夏洛特针刺队",
																																																						"底特律震动队",
																																																						"康涅狄格太阳队",
																																																						"拉斯维加斯王牌队",
																																																						"明尼苏达山猫队",
																																																						"波特兰火焰队",
																																																						"洛杉矶火花队",
																																																						"纽约自由人队",
																																																						"芝加哥天空队",
																																																						"菲尼克斯水星队",
																																																						"萨克拉门托君主队",
																																																						"西雅图风暴队",
																																																						"达拉斯飞翼队",
																																																						"迈阿密太阳神队"
																																																					]
																																																				}
																																																			}
																																																		}
																																																	}
																																																}
																																															},
																																															"else": {
																																																"if": {
																																																	"required": [
																																																		"league_name"
																																																	],
																																																	"properties": {
																																																		"league_name": {
																																																			"contains": {
																																																				"required": [
																																																					"value"
																																																				],
																																																				"properties": {
																																																					"value": {
																																																						"enum": [
																																																							"mlb",
																																																							"MLB",
																																																							"Ligue majeure de baseball",
																																																							"دوري البيسبول الرئيسي"
																																																						]
																																																					}
																																																				}
																																																			}
																																																		}
																																																	}
																																																},
																																																"then": {
																																																	"properties": {
																																																		"team_name": {
																																																			"items": {
																																																				"properties": {
																																																					"value": {
																																																						"enum": [
																																																							"亚利桑那响尾蛇队",
																																																							"亚特兰大勇士队",
																																																							"休斯顿太空人队",
																																																							"克利夫兰纳普斯队",
																																																							"克利夫兰蓝队",
																																																							"克里夫兰印地安人队",
																																																							"加州天使队",
																																																							"匹兹堡海盗队",
																																																							"华盛顿国民队",
																																																							"圣地亚哥教士队",
																																																							"圣路易斯红雀队",
																																																							"坦帕湾光芒队",
																																																							"堪萨斯城田径队",
																																																							"堪萨斯城皇家队",
																																																							"多伦多蓝鸟队",
																																																							"奥克兰运动家队",
																																																							"密尔沃基勇士队",
																																																							"密尔瓦基酿酒人队",
																																																							"巴尔的摩金莺队",
																																																							"底特律老虎队",
																																																							"德克萨斯巡游者队",
																																																							"旧金山巨人队",
																																																							"明尼苏达双城队",
																																																							"波士顿焗豆队",
																																																							"波士顿红袜队",
																																																							"波士顿食豆人队",
																																																							"洛杉矶天使队",
																																																							"洛杉矶道奇队",
																																																							"科罗拉多洛矶山脉",
																																																							"纽约大都会队",
																																																							"纽约洋基队",
																																																							"纽约高地人队",
																																																							"芝加哥小熊队",
																																																							"芝加哥白袜队",
																																																							"芝加哥白长袜队",
																																																							"蒙特利尔博览会队",
																																																							"西雅图水手队",
																																																							"费城费城人队",
																																																							"费城运动家队",
																																																							"辛辛那提红人队",
																																																							"辛辛那提红腿队",
																																																							"迈阿密马林鱼队"
																																																						]
																																																					}
																																																				}
																																																			}
																																																		}
																																																	}
																																																},
																																																"else": {
																																																	"if": {
																																																		"required": [
																																																			"league_name"
																																																		],
																																																		"properties": {
																																																			"league_name": {
																																																				"contains": {
																																																					"required": [
																																																						"value"
																																																					],
																																																					"properties": {
																																																						"value": {
																																																							"enum": [
																																																								"تنس",
																																																								"Tennis",
																																																								"Tenis",
																																																								"Tênis",
																																																								"テニス",
																																																								"tennis"
																																																							]
																																																						}
																																																					}
																																																				}
																																																			}
																																																		}
																																																	},
																																																	"then": {
																																																		"properties": {
																																																			"team_name": {
																																																				"items": {
																																																					"properties": {
																																																						"value": {
																																																							"enum": [
																																																								"网球"
																																																							]
																																																						}
																																																					}
																																																				}
																																																			}
																																																		}
																																																	}
																																																}
																																															}
																																														}
																																													}
																																												}
																																											}
																																										}
																																									}
																																								}
																																							}
																																						}
																																					}
																																				}
																																			}
																																		}
																																	}
																																}
																															}
																														}
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["TEAM_NAME/SIZE_NAME", "TEAM_NAME/SIZE_NAME/COLOR_NAME",
										"TEAM_NAME"
									]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["team_name"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["SIZE_NAME/SCENT_NAME", "SCENT_NAME"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["scent"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["GRIP_TYPE"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["grip"],
			"properties": {
				"grip": {
					"items": {
						"required": ["type"]
					}
				}
			}
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["master_pack_weight"],
					"properties": {
						"master_pack_weight": {
							"items": {
								"required": ["value"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["master_pack_weight"],
					"properties": {
						"master_pack_weight": {
							"items": {
								"required": ["value"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["master_pack_dimensions"]
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["master_pack_dimensions"],
					"properties": {
						"master_pack_dimensions": {
							"items": {
								"required": ["length"],
								"properties": {
									"length": {
										"items": {
											"required": ["value"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["master_pack_dimensions"],
					"properties": {
						"master_pack_dimensions": {
							"items": {
								"required": ["length"],
								"properties": {
									"length": {
										"items": {
											"required": ["value"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["master_pack_dimensions"],
					"properties": {
						"master_pack_dimensions": {
							"items": {
								"required": ["width"],
								"properties": {
									"width": {
										"items": {
											"required": ["value"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["master_pack_dimensions"],
					"properties": {
						"master_pack_dimensions": {
							"items": {
								"required": ["width"],
								"properties": {
									"width": {
										"items": {
											"required": ["value"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["master_pack_dimensions"],
					"properties": {
						"master_pack_dimensions": {
							"items": {
								"required": ["height"],
								"properties": {
									"height": {
										"items": {
											"required": ["value"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["master_pack_dimensions"],
					"properties": {
						"master_pack_dimensions": {
							"items": {
								"required": ["height"],
								"properties": {
									"height": {
										"items": {
											"required": ["value"]
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["master_pack_weight"]
		}
	}, {
		"if": {
			"not": {
				"required": ["parentage_level"],
				"properties": {
					"parentage_level": {
						"contains": {
							"required": ["value"],
							"properties": {
								"value": {
									"enum": ["child"]
								}
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"child_parent_sku_relationship": {
					"items": {
						"not": {
							"required": ["parent_sku"]
						}
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["parentage_level"],
			"properties": {
				"parentage_level": {
					"contains": {
						"required": ["value"],
						"properties": {
							"value": {
								"enum": ["child"]
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"child_parent_sku_relationship": {
					"items": {
						"required": ["parent_sku"]
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["parentage_level"],
			"properties": {
				"parentage_level": {
					"items": {
						"required": ["value"]
					}
				}
			}
		},
		"then": {
			"required": ["child_parent_sku_relationship"]
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"contains": {
							"required": ["child_relationship_type"],
							"properties": {
								"child_relationship_type": {
									"enum": ["variation"]
								}
							}
						}
					}
				}
			}, {
				"required": ["parentage_level"],
				"properties": {
					"parentage_level": {
						"items": {
							"required": ["value"]
						}
					}
				}
			}]
		},
		"then": {
			"required": ["variation_theme"]
		}
	}, {
		"if": {
			"anyOf": [{
				"required": ["fulfillment_availability"],
				"properties": {
					"fulfillment_availability": {
						"contains": {
							"required": ["fulfillment_channel_code"],
							"properties": {
								"fulfillment_channel_code": {
									"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
								}
							}
						}
					}
				}
			}, {
				"not": {
					"required": ["fulfillment_availability"],
					"properties": {
						"fulfillment_availability": {
							"items": {
								"required": ["fulfillment_channel_code"]
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["batteries_required"]
		}
	}, {
		"if": {
			"required": ["batteries_required"],
			"properties": {
				"batteries_required": {
					"contains": {
						"required": ["value"],
						"properties": {
							"value": {
								"enum": [true]
							}
						}
					}
				}
			}
		},
		"then": {
			"required": ["batteries_included"]
		}
	}, {
		"if": {
			"required": ["batteries_included"],
			"properties": {
				"batteries_included": {
					"contains": {
						"required": ["value"],
						"properties": {
							"value": {
								"enum": [true]
							}
						}
					}
				}
			}
		},
		"then": {
			"required": ["battery"],
			"properties": {
				"battery": {
					"items": {
						"required": ["cell_composition"]
					}
				}
			}
		}
	}, {
		"if": {
			"not": {
				"required": ["battery"],
				"properties": {
					"battery": {
						"contains": {
							"required": ["cell_composition"],
							"properties": {
								"cell_composition": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["other_than_listed"]
											}
										}
									}
								}
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"battery": {
					"items": {
						"properties": {
							"cell_composition_other_than_listed": {
								"not": {
									"required": ["value"]
								}
							}
						}
					}
				}
			}
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["battery"],
					"properties": {
						"battery": {
							"contains": {
								"required": ["cell_composition"],
								"properties": {
									"cell_composition": {
										"contains": {
											"required": ["value"],
											"properties": {
												"value": {
													"enum": ["other_than_listed"]
												}
											}
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["battery"],
					"properties": {
						"battery": {
							"contains": {
								"required": ["cell_composition"],
								"properties": {
									"cell_composition": {
										"contains": {
											"required": ["value"],
											"properties": {
												"value": {
													"enum": ["other_than_listed"]
												}
											}
										}
									}
								}
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["battery"],
			"properties": {
				"battery": {
					"items": {
						"required": ["cell_composition_other_than_listed"]
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["battery"],
			"properties": {
				"battery": {
					"contains": {
						"required": ["cell_composition"],
						"properties": {
							"cell_composition": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["lithium", "lithium_air", "lithium_cobalt", "lithium_ion",
												"lithium_manganese_dioxide", "lithium_metal",
												"lithium_nickel_cobalt_aluminum",
												"lithium_nickel_manganese_cobalt", "lithium_phosphate",
												"lithium_polymer", "lithium_thionyl_chloride",
												"lithium_titanate", "polymer"
											]
										}
									}
								}
							}
						}
					}
				}
			}
		},
		"then": {
			"required": ["battery", "lithium_battery", "num_batteries"],
			"properties": {
				"battery": {
					"items": {
						"required": ["weight"]
					}
				},
				"lithium_battery": {
					"items": {
						"required": ["packaging"]
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["battery"],
			"properties": {
				"battery": {
					"contains": {
						"required": ["cell_composition"],
						"properties": {
							"cell_composition": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["lithium", "lithium_manganese_dioxide", "lithium_metal",
												"lithium_thionyl_chloride"
											]
										}
									}
								}
							}
						}
					}
				}
			}
		},
		"then": {
			"required": ["lithium_battery", "number_of_lithium_metal_cells"],
			"properties": {
				"lithium_battery": {
					"items": {
						"required": ["weight"]
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["battery"],
			"properties": {
				"battery": {
					"contains": {
						"required": ["cell_composition"],
						"properties": {
							"cell_composition": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["lithium", "lithium_air", "lithium_cobalt", "lithium_ion",
												"lithium_nickel_cobalt_aluminum",
												"lithium_nickel_manganese_cobalt", "lithium_phosphate",
												"lithium_polymer", "lithium_titanate", "polymer"
											]
										}
									}
								}
							}
						}
					}
				}
			}
		},
		"then": {
			"required": ["lithium_battery", "number_of_lithium_ion_cells"],
			"properties": {
				"lithium_battery": {
					"items": {
						"required": ["energy_content"]
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["supplier_declared_dg_hz_regulation"],
			"properties": {
				"supplier_declared_dg_hz_regulation": {
					"contains": {
						"required": ["value"],
						"properties": {
							"value": {
								"enum": ["not_applicable"]
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"supplier_declared_dg_hz_regulation": {
					"items": {
						"properties": {
							"value": {
								"not": {
									"enum": ["other", "storage", "transportation", "waste"]
								}
							}
						}
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["supplier_declared_dg_hz_regulation"],
			"properties": {
				"supplier_declared_dg_hz_regulation": {
					"contains": {
						"required": ["value"],
						"properties": {
							"value": {
								"enum": ["ghs"]
							}
						}
					}
				}
			}
		},
		"then": {
			"required": ["ghs", "safety_data_sheet_url"],
			"properties": {
				"ghs": {
					"items": {
						"required": ["classification"]
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["supplier_declared_dg_hz_regulation"],
			"properties": {
				"supplier_declared_dg_hz_regulation": {
					"contains": {
						"required": ["value"],
						"properties": {
							"value": {
								"enum": ["transportation"]
							}
						}
					}
				}
			}
		},
		"then": {
			"required": ["hazmat"],
			"properties": {
				"hazmat": {
					"contains": {
						"required": ["aspect"],
						"properties": {
							"aspect": {
								"enum": ["united_nations_regulatory_id"]
							}
						}
					}
				}
			}
		}
	}, {
		"if": {
			"allOf": [{
				"required": ["child_parent_sku_relationship"],
				"properties": {
					"child_parent_sku_relationship": {
						"items": {
							"required": ["parent_sku"]
						}
					}
				}
			}, {
				"not": {
					"required": ["parentage_level"],
					"properties": {
						"parentage_level": {
							"contains": {
								"required": ["value"],
								"properties": {
									"value": {
										"enum": ["parent"]
									}
								}
							}
						}
					}
				}
			}, {
				"required": ["variation_theme"],
				"properties": {
					"variation_theme": {
						"contains": {
							"required": ["name"],
							"properties": {
								"name": {
									"enum": ["ITEM_WEIGHT"]
								}
							}
						}
					}
				}
			}]
		},
		"then": {
			"required": ["item_weight"]
		}
	}, {
		"properties": {
			"california_proposition_65": {
				"items": {
					"if": {
						"required": ["compliance_type"],
						"properties": {
							"compliance_type": {
								"enum": ["food", "furniture", "chemical"]
							}
						}
					},
					"then": {
						"required": ["chemical_names"],
						"properties": {
							"chemical_names": {
								"minItems": 1
							}
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"california_proposition_65": {
				"items": {
					"if": {
						"required": ["compliance_type"],
						"properties": {
							"compliance_type": {
								"enum": ["food", "furniture", "chemical"]
							}
						}
					},
					"then": {
						"properties": {
							"chemical_names": {
								"maxItems": 1
							}
						}
					},
					"else": {
						"not": {
							"required": ["chemical_names"]
						}
					}
				}
			}
		}
	}, {
		"properties": {
			"pesticide_marking": {
				"items": {
					"if": {
						"required": ["registration_status"],
						"properties": {
							"registration_status": {
								"enum": ["fifra_registration_required"]
							}
						}
					},
					"then": {
						"required": ["certification_number"]
					}
				}
			}
		}
	}, {
		"properties": {
			"pesticide_marking": {
				"items": {
					"if": {
						"required": ["marking_type"],
						"properties": {
							"marking_type": {
								"enum": ["epa_establishment_number", "epa_registration_number"]
							}
						}
					},
					"then": {
						"required": ["registration_status"]
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["fcc_radio_frequency_emission_compliance"],
			"properties": {
				"fcc_radio_frequency_emission_compliance": {
					"contains": {
						"required": ["registration_status"],
						"properties": {
							"registration_status": {
								"enum": ["has_fcc_id"]
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"fcc_radio_frequency_emission_compliance": {
					"items": {
						"required": ["identification_number"]
					}
				}
			}
		}
	}, {
		"if": {
			"required": ["fcc_radio_frequency_emission_compliance"],
			"properties": {
				"fcc_radio_frequency_emission_compliance": {
					"contains": {
						"required": ["registration_status"],
						"properties": {
							"registration_status": {
								"enum": ["fcc_supplier_declaration_of_conformity"]
							}
						}
					}
				}
			}
		},
		"then": {
			"properties": {
				"fcc_radio_frequency_emission_compliance": {
					"items": {
						"required": ["point_of_contact_address", "point_of_contact_email",
							"point_of_contact_name", "point_of_contact_phone_number"
						]
					}
				}
			}
		}
	}, {
		"allOf": [{
			"if": {
				"anyOf": [{
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
										}
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"items": {
										"required": ["value"]
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
										}
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["parent"]
											}
										}
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"not": {
							"required": ["fulfillment_availability"],
							"properties": {
								"fulfillment_availability": {
									"items": {
										"required": ["fulfillment_channel_code"]
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"items": {
										"required": ["value"]
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"not": {
							"required": ["fulfillment_availability"],
							"properties": {
								"fulfillment_availability": {
									"items": {
										"required": ["fulfillment_channel_code"]
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["parent"]
											}
										}
									}
								}
							}
						}
					}]
				}]
			},
			"then": {
				"required": ["item_package_dimensions"]
			}
		}, {
			"if": {
				"anyOf": [{
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["DEFAULT"]
										}
									}
								}
							}
						}
					}, {
						"required": ["package_level"],
						"properties": {
							"package_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"items": {
										"required": ["value"]
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["DEFAULT"]
										}
									}
								}
							}
						}
					}, {
						"required": ["package_level"],
						"properties": {
							"package_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["parent"]
											}
										}
									}
								}
							}
						}
					}]
				}]
			},
			"then": {
				"required": ["item_package_dimensions"]
			}
		}]
	}, {
		"allOf": [{
			"if": {
				"anyOf": [{
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
										}
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"items": {
										"required": ["value"]
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["AMAZON_NA", "AMAZON_US_TBYB"]
										}
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["parent"]
											}
										}
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"not": {
							"required": ["fulfillment_availability"],
							"properties": {
								"fulfillment_availability": {
									"items": {
										"required": ["fulfillment_channel_code"]
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"items": {
										"required": ["value"]
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"not": {
							"required": ["fulfillment_availability"],
							"properties": {
								"fulfillment_availability": {
									"items": {
										"required": ["fulfillment_channel_code"]
									}
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["parent"]
											}
										}
									}
								}
							}
						}
					}]
				}]
			},
			"then": {
				"required": ["item_package_weight"]
			}
		}, {
			"if": {
				"anyOf": [{
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["DEFAULT"]
										}
									}
								}
							}
						}
					}, {
						"required": ["package_level"],
						"properties": {
							"package_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"items": {
										"required": ["value"]
									}
								}
							}
						}
					}]
				}, {
					"allOf": [{
						"required": ["fulfillment_availability"],
						"properties": {
							"fulfillment_availability": {
								"contains": {
									"required": ["fulfillment_channel_code"],
									"properties": {
										"fulfillment_channel_code": {
											"enum": ["DEFAULT"]
										}
									}
								}
							}
						}
					}, {
						"required": ["package_level"],
						"properties": {
							"package_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}, {
						"not": {
							"required": ["parentage_level"],
							"properties": {
								"parentage_level": {
									"contains": {
										"required": ["value"],
										"properties": {
											"value": {
												"enum": ["parent"]
											}
										}
									}
								}
							}
						}
					}]
				}]
			},
			"then": {
				"required": ["item_package_weight"]
			}
		}]
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["master_packs_per_layer_quantity"],
					"properties": {
						"master_packs_per_layer_quantity": {
							"items": {
								"required": ["value"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["master_packs_per_layer_quantity"],
					"properties": {
						"master_packs_per_layer_quantity": {
							"items": {
								"required": ["value"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["master_pack_layers_per_pallet_quantity"]
		}
	}, {
		"if": {
			"anyOf": [{
				"allOf": [{
					"required": ["master_pack_layers_per_pallet_quantity"],
					"properties": {
						"master_pack_layers_per_pallet_quantity": {
							"items": {
								"required": ["value"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"items": {
									"required": ["value"]
								}
							}
						}
					}
				}]
			}, {
				"allOf": [{
					"required": ["master_pack_layers_per_pallet_quantity"],
					"properties": {
						"master_pack_layers_per_pallet_quantity": {
							"items": {
								"required": ["value"]
							}
						}
					}
				}, {
					"not": {
						"required": ["parentage_level"],
						"properties": {
							"parentage_level": {
								"contains": {
									"required": ["value"],
									"properties": {
										"value": {
											"enum": ["parent"]
										}
									}
								}
							}
						}
					}
				}]
			}]
		},
		"then": {
			"required": ["master_packs_per_layer_quantity"]
		}
	}]
}