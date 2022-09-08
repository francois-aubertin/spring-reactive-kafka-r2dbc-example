
create table fx_order (
  order_id bigserial primary key,
  buy_sell varchar(4) not null,
  base_ccy char(3) not null,
  quote_ccy char(3) not null,
  quantity numeric not null
);
