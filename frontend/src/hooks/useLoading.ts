import { ref } from 'vue';

/**
 * 通用加载状态 hook
 */
export default function useLoading(initValue = false) {
  const loading = ref(initValue);

  const setLoading = (value: boolean) => {
    loading.value = value;
  };

  const toggle = () => {
    loading.value = !loading.value;
  };

  return { loading, setLoading, toggle };
}
