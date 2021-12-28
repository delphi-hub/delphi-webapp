<template>
  <div>
    <h2 class="w-full text-delphi-red text-2xl font-semibold font-serif">
      {{ groupId }}
    </h2>

    <div v-if="isLoading">Loading...</div>
    <div v-else class="text-xs line-height-xs">
      <table class="text-left">
        <tr>
          <th scope="col">Artifacts</th>
        </tr>
        <tr v-for="(a, index) in group" :key="index">
          <td>
            <router-link
              :to="{
                name: 'maven-ga',
                params: {
                  groupId: groupId,
                  artifactId: a,
                },
              }"
              >{{ a }}</router-link
            >
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>
<script lang="ts">
import { MavenAccess } from "@/backend/maven/Maven";
import { defineComponent, onBeforeMount, ref, watch } from "vue";
import { useRoute } from "vue-router";

export default defineComponent({
  setup() {
    const route = useRoute();
    const groupId = ref("");

    const isLoading = ref(false);
    const group = ref(null);

    onBeforeMount(async () => {
      processRouteValues();
      await loadGroup();
    });

    watch(
      () => route.params,
      async () => {
        processRouteValues();
        await loadGroup();
      }
    );

    function processRouteValues(): void {
      groupId.value = route.params.groupId as string;
    }

    async function loadGroup() {
      if (groupId.value === undefined) return;
      isLoading.value = true;
      group.value = await new MavenAccess().getGroup(groupId.value);
      isLoading.value = false;
    }

    return { groupId, isLoading, group };
  },
});
</script>
