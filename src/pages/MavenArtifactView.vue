<template>
  <div>
    <h2 class="w-full text-delphi-red text-2xl font-semibold font-serif">
      <router-link :to="{ name: 'maven-g', params: { groupId: groupId } }">{{
        groupId
      }}</router-link>
      | {{ artifactId }}
    </h2>

    <div v-if="isLoading">Loading...</div>
    <div v-else class="text-xs line-height-xs">
        <table class="text-left">
        <caption class="text-left font-bold">
          Versions
        </caption>
        <tr v-for="(v, index) in artifact" :key="index">
          <td>
            <router-link
              :to="{
                name: 'maven-gav',
                params: {
                  groupId: groupId,
                  artifactId: artifactId,
                  version: v,
                },
              }"
              >{{ v }}</router-link>
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
    const artifactId = ref("");

    const isLoading = ref(false);
    const artifact = ref(null);

    onBeforeMount(async () => {
      processRouteValues();
      await loadArtifact();
    });

    watch(
      () => route.params,
      async () => {
        processRouteValues();
        await loadArtifact();
      }
    );

    function processRouteValues(): void {
      groupId.value = route.params.groupId as string;
      artifactId.value = route.params.artifactId as string;
    }

    async function loadArtifact() {
        if (groupId.value === undefined || 
          artifactId.value === undefined) return;
        isLoading.value = true;
        artifact.value = await new MavenAccess().getArtifact(groupId.value, artifactId.value);
        isLoading.value = false;
    }

    return { groupId, artifactId, isLoading, artifact };
  },
});
</script>
