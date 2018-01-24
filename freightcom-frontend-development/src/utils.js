import { SubmissionError } from 'redux-form';

export const deepFreeze = obj => {
  let propNames = Object.getOwnPropertyNames(obj);

  propNames.forEach(function(name) {
    let prop = obj[name];

    if (typeof prop === 'object' && prop !== null) deepFreeze(prop);
  });

  return Object.freeze(obj);
};

export const queryString = params => {
  return Object.keys(params)
    .reduce((qs, kv) => {
      if (params[kv] || params[kv] === 0) {
        qs.push(`${kv}=${params[kv]}`);
      }
      return qs;
    }, [])
    .join('&');
};

export const buildURL = (
  basename = '/',
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrderList = {},
) => {
  let url =
    basename +
    '?' +
    queryString({ ...filters, page: pageToLoad - 1, size: itemsPerPage });

  (Array.isArray(sortOrderList)
    ? sortOrderList
    : [sortOrderList]).map(sortOrder => {
    if (sortOrder && sortOrder.sortBy) {
      url +=
        '&sort=' +
        sortOrder.sortBy +
        ',' +
        (sortOrder.order ? sortOrder.order : 'asc');
    }

    return true;
  });

  return url;
};

export const ucWords = str => {
  return (str +
    '').replace(/^([a-z\u00E0-\u00FC])|\s+([a-z\u00E0-\u00FC])/g, function($1) {
    return $1.toUpperCase();
  });
};

// Level 1: display all
// Level 2: display street level address
// Level 3: display city, postalCode, province, country
export const formatAddress = (address, level = 1) => {
  let formattedAddress = '';
  let level3Address = '';
  if (address) {
    if (address.address) {
      formattedAddress = address.address + ', ';
    } else {
      if (address.address1) formattedAddress = address.address1 + ', ';

      if (address.address2) formattedAddress += address.address2 + ', ';
    }

    if (level === 2) {
      return formattedAddress;
    }

    if (address.city) {
      formattedAddress += address.city + ', ';
      level3Address += address.city + ', ';
    }
    if (address.province) {
      formattedAddress += address.province + ', ';
      level3Address += address.province + ', ';
    }
    if (address.country) {
      formattedAddress += address.country;
      level3Address += address.country + ', ';
    }
    if (address.postcode) {
      formattedAddress += address.postcode + ', ';
      level3Address += address.postcode + ', ';
    }
  }
  if (level === 3) {
    return level3Address;
  }

  return formattedAddress;
};

export const formatPhone = phone => {
  return !phone
    ? ''
    : phone.replace(/[^0-9]/g, '').replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
};

export const formatCurrency = amount => {
  if (amount) {
    return `${(parseFloat(amount, 10) || 0).toFixed(2).toLocaleString()}`;
  }
  return '0.00';
};

export const displayCompanyOrContactName = data => {
  const {
    consigneeName = '',
    company = '',
    companyName = '',
    contactName = '',
    attention = '',
  } = data || {};

  return (
    consigneeName || company || companyName || contactName || attention || ''
  );
};

export const formatDate = date => {};

export const states = {
  '': '',
  AL: 'Alabama',
  AK: 'Alaska',
  AZ: 'Arizona',
  AR: 'Arkansas',
  CA: 'California',
  CO: 'Colorado',
  CT: 'Connecticut',
  DE: 'Delaware',
  DC: 'District Of Columbia',
  FL: 'Florida',
  GA: 'Georgia',
  HI: 'Hawaii',
  ID: 'Idaho',
  IL: 'Illinois',
  IN: 'Indiana',
  IA: 'Iowa',
  KS: 'Kansas',
  KY: 'Kentucky',
  LA: 'Louisiana',
  ME: 'Maine',
  MD: 'Maryland',
  MA: 'Massachusetts',
  MI: 'Michigan',
  MN: 'Minnesota',
  MS: 'Mississippi',
  MO: 'Missouri',
  MT: 'Montana',
  NE: 'Nebraska',
  NV: 'Nevada',
  NH: 'New Hampshire',
  NJ: 'New Jersey',
  NM: 'New Mexico',
  NY: 'New York',
  NC: 'North Carolina',
  ND: 'North Dakota',
  OH: 'Ohio',
  OK: 'Oklahoma',
  OR: 'Oregon',
  PA: 'Pennsylvania',
  RI: 'Rhode Island',
  SC: 'South Carolina',
  SD: 'South Dakota',
  TN: 'Tennessee',
  TX: 'Texas',
  UT: 'Utah',
  VT: 'Vermont',
  VA: 'Virginia',
  WA: 'Washington',
  WV: 'West Virginia',
  WI: 'Wisconsin',
  WY: 'Wyoming',
};

export const provinces = {
  '': '',
  AB: 'Alberta',
  BC: 'British Columbia',
  MB: 'Manitoba',
  NB: 'New Brunswick',
  NL: 'Newfoundland and Labrador',
  NS: 'Nova Scotia',
  NT: 'Northwest Territories',
  NU: 'Nunavut',
  ON: 'Ontario',
  PE: 'Prince Edward Island',
  QC: 'Quebec',
  SK: 'Saskatchewan',
  YT: 'Yukon Territory',
};

//export const countries = { CA: 'Canada', US: 'USA' };
export const countries = {
  '': '',
  CA: 'Canada',
  US: 'United States',
  AF: 'Afghanistan',
  AX: 'Aland Islands',
  AL: 'Albania',
  DZ: 'Algeria',
  AS: 'American Samoa',
  AD: 'Andorra',
  AO: 'Angola',
  AI: 'Anguilla',
  AQ: 'Antarctica',
  AG: 'Antigua And Barbuda',
  AR: 'Argentina',
  AM: 'Armenia',
  AW: 'Aruba',
  AU: 'Australia',
  AT: 'Austria',
  AZ: 'Azerbaijan',
  BS: 'Bahamas',
  BH: 'Bahrain',
  BD: 'Bangladesh',
  BB: 'Barbados',
  BY: 'Belarus',
  BE: 'Belgium',
  BZ: 'Belize',
  BJ: 'Benin',
  BM: 'Bermuda',
  BT: 'Bhutan',
  BO: 'Bolivia',
  BA: 'Bosnia And Herzegovina',
  BW: 'Botswana',
  BV: 'Bouvet Island',
  BR: 'Brazil',
  IO: 'British Indian Ocean Territory',
  BN: 'Brunei Darussalam',
  BG: 'Bulgaria',
  BF: 'Burkina Faso',
  BI: 'Burundi',
  KH: 'Cambodia',
  CM: 'Cameroon',
  CV: 'Cape Verde',
  KY: 'Cayman Islands',
  CF: 'Central African Republic',
  TD: 'Chad',
  CL: 'Chile',
  CN: 'China',
  CX: 'Christmas Island',
  CC: 'Cocos (Keeling) Islands',
  CO: 'Colombia',
  KM: 'Comoros',
  CG: 'Congo',
  CD: 'Congo, Democratic Republic',
  CK: 'Cook Islands',
  CR: 'Costa Rica',
  CI: "Cote D'Ivoire",
  HR: 'Croatia',
  CU: 'Cuba',
  CY: 'Cyprus',
  CZ: 'Czech Republic',
  DK: 'Denmark',
  DJ: 'Djibouti',
  DM: 'Dominica',
  DO: 'Dominican Republic',
  EC: 'Ecuador',
  EG: 'Egypt',
  SV: 'El Salvador',
  GQ: 'Equatorial Guinea',
  ER: 'Eritrea',
  EE: 'Estonia',
  ET: 'Ethiopia',
  FK: 'Falkland Islands (Malvinas)',
  FO: 'Faroe Islands',
  FJ: 'Fiji',
  FI: 'Finland',
  FR: 'France',
  GF: 'French Guiana',
  PF: 'French Polynesia',
  TF: 'French Southern Territories',
  GA: 'Gabon',
  GM: 'Gambia',
  GE: 'Georgia',
  DE: 'Germany',
  GH: 'Ghana',
  GI: 'Gibraltar',
  GR: 'Greece',
  GL: 'Greenland',
  GD: 'Grenada',
  GP: 'Guadeloupe',
  GU: 'Guam',
  GT: 'Guatemala',
  GG: 'Guernsey',
  GN: 'Guinea',
  GW: 'Guinea-Bissau',
  GY: 'Guyana',
  HT: 'Haiti',
  HM: 'Heard Island & Mcdonald Islands',
  VA: 'Holy See (Vatican City State)',
  HN: 'Honduras',
  HK: 'Hong Kong',
  HU: 'Hungary',
  IS: 'Iceland',
  IN: 'India',
  ID: 'Indonesia',
  IR: 'Iran, Islamic Republic Of',
  IQ: 'Iraq',
  IE: 'Ireland',
  IM: 'Isle Of Man',
  IL: 'Israel',
  IT: 'Italy',
  JM: 'Jamaica',
  JP: 'Japan',
  JE: 'Jersey',
  JO: 'Jordan',
  KZ: 'Kazakhstan',
  KE: 'Kenya',
  KI: 'Kiribati',
  KR: 'Korea',
  KW: 'Kuwait',
  KG: 'Kyrgyzstan',
  LA: "Lao People's Democratic Republic",
  LV: 'Latvia',
  LB: 'Lebanon',
  LS: 'Lesotho',
  LR: 'Liberia',
  LY: 'Libyan Arab Jamahiriya',
  LI: 'Liechtenstein',
  LT: 'Lithuania',
  LU: 'Luxembourg',
  MO: 'Macao',
  MK: 'Macedonia',
  MG: 'Madagascar',
  MW: 'Malawi',
  MY: 'Malaysia',
  MV: 'Maldives',
  ML: 'Mali',
  MT: 'Malta',
  MH: 'Marshall Islands',
  MQ: 'Martinique',
  MR: 'Mauritania',
  MU: 'Mauritius',
  YT: 'Mayotte',
  MX: 'Mexico',
  FM: 'Micronesia, Federated States Of',
  MD: 'Moldova',
  MC: 'Monaco',
  MN: 'Mongolia',
  ME: 'Montenegro',
  MS: 'Montserrat',
  MA: 'Morocco',
  MZ: 'Mozambique',
  MM: 'Myanmar',
  NA: 'Namibia',
  NR: 'Nauru',
  NP: 'Nepal',
  NL: 'Netherlands',
  AN: 'Netherlands Antilles',
  NC: 'New Caledonia',
  NZ: 'New Zealand',
  NI: 'Nicaragua',
  NE: 'Niger',
  NG: 'Nigeria',
  NU: 'Niue',
  NF: 'Norfolk Island',
  MP: 'Northern Mariana Islands',
  NO: 'Norway',
  OM: 'Oman',
  PK: 'Pakistan',
  PW: 'Palau',
  PS: 'Palestinian Territory, Occupied',
  PA: 'Panama',
  PG: 'Papua New Guinea',
  PY: 'Paraguay',
  PE: 'Peru',
  PH: 'Philippines',
  PN: 'Pitcairn',
  PL: 'Poland',
  PT: 'Portugal',
  PR: 'Puerto Rico',
  QA: 'Qatar',
  RE: 'Reunion',
  RO: 'Romania',
  RU: 'Russian Federation',
  RW: 'Rwanda',
  BL: 'Saint Barthelemy',
  SH: 'Saint Helena',
  KN: 'Saint Kitts And Nevis',
  LC: 'Saint Lucia',
  MF: 'Saint Martin',
  PM: 'Saint Pierre And Miquelon',
  VC: 'Saint Vincent And Grenadines',
  WS: 'Samoa',
  SM: 'San Marino',
  ST: 'Sao Tome And Principe',
  SA: 'Saudi Arabia',
  SN: 'Senegal',
  RS: 'Serbia',
  SC: 'Seychelles',
  SL: 'Sierra Leone',
  SG: 'Singapore',
  SK: 'Slovakia',
  SI: 'Slovenia',
  SB: 'Solomon Islands',
  SO: 'Somalia',
  ZA: 'South Africa',
  GS: 'South Georgia And Sandwich Isl.',
  ES: 'Spain',
  LK: 'Sri Lanka',
  SD: 'Sudan',
  SR: 'Suriname',
  SJ: 'Svalbard And Jan Mayen',
  SZ: 'Swaziland',
  SE: 'Sweden',
  CH: 'Switzerland',
  SY: 'Syrian Arab Republic',
  TW: 'Taiwan',
  TJ: 'Tajikistan',
  TZ: 'Tanzania',
  TH: 'Thailand',
  TL: 'Timor-Leste',
  TG: 'Togo',
  TK: 'Tokelau',
  TO: 'Tonga',
  TT: 'Trinidad And Tobago',
  TN: 'Tunisia',
  TR: 'Turkey',
  TM: 'Turkmenistan',
  TC: 'Turks And Caicos Islands',
  TV: 'Tuvalu',
  UG: 'Uganda',
  UA: 'Ukraine',
  AE: 'United Arab Emirates',
  GB: 'United Kingdom',
  UM: 'United States Outlying Islands',
  UY: 'Uruguay',
  UZ: 'Uzbekistan',
  VU: 'Vanuatu',
  VE: 'Venezuela',
  VN: 'Viet Nam',
  VG: 'Virgin Islands, British',
  VI: 'Virgin Islands, U.S.',
  WF: 'Wallis And Futuna',
  EH: 'Western Sahara',
  YE: 'Yemen',
  ZM: 'Zambia',
  ZW: 'Zimbabwe',
};

export const formatCountry = country => {
  if (country) {
    switch (true) {
      case country.toUpperCase() === 'CA':
        return 'Canada';

      case country.toUpperCase() === 'US':
        return 'USA';

      default:
        return country;
    }
  }
};

export const formatProvince = province => {
  if (province) {
    if (province === 'PQ') {
      return 'Quebec';
    }
    // for (let [k, v] of Object.entries(Object.assign(provinces, states))) {
    //   if (province === k) {
    //     return v;
    //   }
    // }
  }
  return province;
};

export const formatRole = role => {
  switch (true) {
    case role === 'CUSTOMER_ADMIN':
      return 'Admin';
    case role === 'CUSTOMER_STAFF':
      return 'Staff';
    case role === 'AGENT':
      return 'Agent';
    case role === 'ADMIN':
      return 'Freightcom Admin';
    case role === 'FREIGHTCOM_STAFF':
      return 'Freightcom Staff';
    default:
      return role;
  }
};

const value_add = (keys, values, result) => {
  let [key, ...rest] = keys;

  if (!rest.length) {
    result[key] = values ? values[key] : null;
  } else {
    if (!result[key]) {
      result[key] = {};

      if (values[key] && values[key].id) {
        result[key].id = values[key].id;
      }

      if (values[key] && values[key].addressId) {
        result[key].addressId = values[key].addressId;
      }
    }

    value_add(rest, values ? values[key] : null, result[key]);
  }

  return result;
};

export const form_pick = (values, names) => {
  let result = {};

  names.forEach(name => {
    value_add(name.split('.'), values, result);
  });

  return result;
};

export const shipmentStatus = [
  { id: 16, label: 'DRAFT', actions: ['delete'] },
  { id: 10, label: 'QUOTED DRAFT', actions: ['delete'] },
  { id: 19, label: 'SUBMITTED', actions: ['schedule-pickup'] },
  { id: 1, label: 'READY FOR SHIPPING', actions: ['cancel', 'missed'] },
  { id: 2, label: 'IN TRANSIT', actions: [] },
  { id: 3, label: 'DELIVERED', actions: ['claim'] },
  { id: 4, label: 'CANCELLED', actions: [] },
  { id: 6, label: 'PREDISPATCHED', actions: ['cancel'] },
  { id: 7, label: 'READY TO INVOICE', actions: [] },
  { id: 15, label: 'INVOICED', actions: ['dispute'] },
  { id: 8, label: 'READY FOR DISPATCH', actions: [] },
  { id: 9, label: 'MISSED PICKUP', actions: ['reschedule'] },
  { id: 999, label: 'DELETED', actions: ['reopen'] },
];

export const billingStatus = [
  { id: 15, label: 'INVOICED', actions: ['dispute'] },
  { id: 7, label: 'READY TO INVOICE', actions: [] },
  { id: 999, label: 'NOT READY TO INVOICE', actions: ['reopen'] },
];

export const getAgentRoleOfUser = userObject => {
  const roles = userObject ? userObject.authorities : [];
  const agentRole = roles.find(role => role.roleName === 'AGENT');
  return agentRole || false;
};

export const makeCancelable = promise => {
  let hasCanceled_ = false;

  const wrappedPromise = new Promise((resolve, reject) => {
    promise.then(
      val => (hasCanceled_ ? reject({ isCanceled: true }) : resolve(val)),
    );
    promise.catch(
      error => (hasCanceled_ ? reject({ isCanceled: true }) : reject(error)),
    );
  });

  return {
    promise: wrappedPromise,
    cancel() {
      hasCanceled_ = true;
    },
  };
};

export const apiCallbackHandler = (
  onSuccess = false,
  onError = false,
) => result => {
  if (result.error) {
    if (typeof onError === 'function') {
      onError(result);
    }
  } else {
    if (typeof onSuccess === 'function') {
      onSuccess(result);
    }
  }
};

export const addressBookValidation = values => {
  let errors = {};

  if (!values.consigneeName) {
    errors['consigneeName'] = 'Please enter your company name.';
  }

  if (!values.address1) {
    errors['address1'] = 'Please enter an address.';
  }

  if (!values.city) {
    errors['city'] = 'Please enter a city.';
  }

  if (!values.postalCode) {
    errors['postalCode'] = 'Please enter postal/zip code.';
  } else if (
    !/(^\d{5}(-\d{4})?$)|(^[ABCEGHJKLMNPRSTVXY]{1}\d{1}[A-Z]{1} *\d{1}[A-Z]{1}\d{1}$)/.test(
      values.postalCode,
    )
  ) {
    errors['postalCode'] = 'e.g) A1B2C3, A1B 2C3, 94105-0011, 94105';
  }

  if (!values.province) {
    errors['province'] = 'Please select a province.';
  }

  if (!values.country) {
    errors['country'] = 'Please select a country.';
  }

  if (!values.consigneeName) {
    errors['contactName'] = 'Please enter a contact name.';
  }

  if (!values.phone) {
    errors['phone'] = 'Please enter your phone number.';
  } else if (!/^(\()?\d{3}(\))?(-|\s)?\d{3}(-|\s)\d{4}$/.test(values.phone)) {
    errors['phone'] = 'e.g) (xxx)xxx-xxxx or xxx-xxx-xxxx.';
  }

  if (!values.contactEmail) {
    errors['contactEmail'] = 'Please enter contact email address.';
  } else if (
    !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.contactEmail)
  ) {
    errors['contactEmail'] = 'Please enter a valid email address.';
  }

  if (
    values.contact2Email &&
    !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.contact2Email)
  ) {
    errors['contact2Email'] = 'Please enter a valid email address.';
  }

  if (
    values.contact3Email &&
    !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.contact3Email)
  ) {
    errors['contact3Email'] = 'Please enter a valid email address.';
  }

  return errors;
};

export const booleanToInt = value => {
  return value ? 1 : 0;
};

export const parseAsInt = value => {
  return value === undefined ? undefined : parseInt(value, 10);
};

export const parseAsBoolean = value => {
  return value ? true : false;
};

export const inputMaskNumber = value => {
  return value === 0 || value ? String(value).replace(/[^0-9.]/g, '') : '';
};
export const inputMaskAlphaNumeric = value => {
  return value === 0 || value
    ? String(value).replace(/[^0-9a-zA-Z-_.]/g, '')
    : '';
};

export const displayShipmentType = packageTypeName => {
  switch (packageTypeName) {
    case 'env':
      return 'Letter';
    case 'pak':
      return 'Pak';
    case 'package':
      return 'Package';
    case 'pallet':
      return 'LTL';
    default:
      return 'Unknown';
  }
};

export const submitFilter = values => {
  for (var k in values) {
    if (values.hasOwnProperty(k)) {
      // if (!values[k] || k.match(/at$/gi)) {
      if (
        typeof values[k] === 'undefined' ||
        k.match(/at$/gi) ||
        values[k] === null
      ) {
        delete values[k];
      }
    }
  }
  return values;
};

const formatErrors = response => {
  return {};
};

export const formApiAdaptor = (
  dispatch,
  action,
  onSuccess = false,
  onError = false,
) => {
  return new Promise((resolve, reject) => {
    dispatch(action).then(response => {
      if (response.error) {
        reject(new SubmissionError(formatErrors(response)));
        if (typeof onError === 'function') {
          onError(response);
        }
      } else {
        if (typeof onSuccess === 'function') {
          onSuccess(response);
        }
      }
    });
  });
};

export const fetchAddressFromPostalCode = postalCode => {
  const countryUS = /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(postalCode);
  const countryCA = /^[A-Za-z]\d[A-Za-z][ -]?\d[A-Za-z]\d$/.test(postalCode);

  if (countryUS || countryCA) {
    const googleMapEndpoint =
      'https://maps.googleapis.com/maps/api/geocode/json?sensor=true&key=AIzaSyDwuavQajnKC2lKcL0RKV7dANUtRhl8qHM&address=';
    return fetch(googleMapEndpoint + postalCode)
      .then(res => res.json())
      .then(json => {
        let city, province, country;
        const addressComponents = json.results[0].address_components;
        addressComponents.forEach(item => {
          if (~item.types.indexOf('locality')) {
            city = item.long_name;
          }
          if (~item.types.indexOf('administrative_area_level_1')) {
            province = item.short_name;
          }
          if (~item.types.indexOf('country')) {
            country = item.short_name;
          }
        });
        return Promise.resolve({ postalCode, city, province, country });
      })
      .catch(err => {
        console.log('Error while fetching address...', err);
        return Promise.resolve(false);
      });
  } else {
    //TODO: How to handle other countries
    return Promise.resolve(false);
  }
};

// return copy of object without the specified key
export const objectWithoutKey = (object, key) => {
  const { [key]: deletedKey, ...otherKeys } = object;
  return otherKeys;
};
