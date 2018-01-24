export const removeOneEntity = (entity, id, fieldToDelete) => {
  return {
    type: 'REMOVE_ITEM_FROM_ENTITY',
    payload: { id, entity, fieldToDelete },
  };
};
